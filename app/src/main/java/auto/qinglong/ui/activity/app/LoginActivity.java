package auto.qinglong.ui.activity.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import auto.base.util.TextUnit;
import auto.base.util.ToastUnit;
import auto.base.util.WindowUnit;
import auto.base.view.popup.PopProgressWindow;
import auto.base.view.popup.PopupWindowBuilder;
import auto.qinglong.R;
import auto.qinglong.bean.panel.Account;
import auto.qinglong.bean.panel.SystemInfo;
import auto.qinglong.database.sp.PanelPreference;
import auto.qinglong.net.NetManager;
import auto.qinglong.net.panel.v10.ApiController;
import auto.qinglong.ui.BaseActivity;
import auto.qinglong.utils.WebUnit;

public class LoginActivity extends BaseActivity {
    public static final String TAG = "LoginActivity";

    private ImageView ui_logo;
    private Button ui_confirm;
    private EditText ui_address;
    private EditText ui_username;
    private EditText ui_password;
    private PopProgressWindow ui_pop_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ui_logo = findViewById(R.id.img_logo);
        ui_confirm = findViewById(R.id.bt_confirm);
        ui_address = findViewById(R.id.et_address);
        ui_username = findViewById(R.id.et_username);
        ui_password = findViewById(R.id.et_password);

        init();
    }

    @Override
    protected void onDestroy() {
        //关闭pop 防止内存泄漏
        if (ui_pop_progress != null) {
            ui_pop_progress.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (ui_pop_progress != null && ui_pop_progress.isShowing()) {
            ui_pop_progress.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ui_pop_progress != null && ui_pop_progress.isShowing()) {
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void init() {
        ui_logo.setOnClickListener(v -> WebUnit.open(this, getString(R.string.url_project)));

        ui_password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ui_password.clearFocus();
                ui_confirm.performClick();
                return true;
            }
            return false;
        });

        ui_confirm.setOnClickListener(v -> {
            if (ui_pop_progress != null && ui_pop_progress.isShowing()) {
                return;
            }

            String address = ui_address.getText().toString();

            if (!address.matches("[0-9a-zA-Z.:/_-]+")) {
                ToastUnit.showShort("地址格式错误");
                return;
            }

            String username = ui_username.getText().toString().trim();
            if (username.isEmpty()) {
                ToastUnit.showShort("账号不能为空");
                return;
            }

            String password = ui_password.getText().toString().trim();
            if (password.isEmpty()) {
                ToastUnit.showShort("密码不能为空");
                return;
            }
            WindowUnit.hideKeyboard(ui_password);

            ui_confirm.setEnabled(false);
            ui_confirm.postDelayed(() -> ui_confirm.setEnabled(true), 300);

            if (ui_pop_progress == null) {
                ui_pop_progress = PopupWindowBuilder.buildProgressWindow(this, () -> NetManager.cancelAllCall(getNetRequestID()));
            }
            ui_pop_progress.setTextAndShow("登录中...");

            Account account = new Account(username, password, address, "");
            //账号存在本地则尝试旧token 避免重复登录
            account.setToken(PanelPreference.getAuthorization(address, username, password));
            //检测系统是否初始化和版本信息(延迟500ms)
            new Handler().postDelayed(() -> querySystemInfo(account), 500);

        });

        //显示之前账号
        Account account = PanelPreference.getCurrentAccount();
        if (account != null) {
            ui_address.setText(account.getAddress());
            ui_username.setText(account.getUsername());
            ui_password.setText(account.getPassword());
        }
    }

    /**
     * 进入主界面
     */
    private void enterHome() {
        Intent intent = new Intent(mContext, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    protected void querySystemInfo(Account account) {
        auto.qinglong.net.panel.ApiController.getSystemInfo(account.getBaseUrl(), new auto.qinglong.net.panel.ApiController.SystemCallBack() {
            @Override
            public void onSuccess(SystemInfo system) {
                PanelPreference.setVersion(system.getVersion());
                if (!system.isInitialized()) {
                    ui_pop_progress.dismiss();
                    ToastUnit.showShort("系统未初始化，无法登录");
                } else if (TextUnit.isFull(account.getToken())) {
                    checkAccountToken(account);
                } else {
                    login(account);
                }
            }

            @Override
            public void onFailure(String msg) {
                ui_pop_progress.dismiss();
                ToastUnit.showShort(msg);
            }
        });
    }

    protected void checkAccountToken(Account account) {
        auto.qinglong.net.panel.ApiController.checkAccountToken(account.getBaseUrl(), account.getAuthorization(), new auto.qinglong.net.panel.ApiController.BaseCallBack() {
            @Override
            public void onSuccess() {
                enterHome();
            }

            @Override
            public void onFailure(String msg) {
                login(account);
            }
        });
    }

    protected void login(Account account) {
        ApiController.login(this.getNetRequestID(), account, new ApiController.NetLoginCallback() {
            @Override
            public void onSuccess(Account account) {
                PanelPreference.updateCurrentAccount(account);
                enterHome();
            }

            @Override
            public void onFailure(String msg) {
                ui_pop_progress.dismiss();
                ToastUnit.showShort(msg);
            }
        });
    }
}