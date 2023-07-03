package auto.qinglong.ui.activity.panel.log;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import auto.base.util.ToastUnit;
import auto.qinglong.R;
import auto.qinglong.bean.panel.LogFile;
import auto.qinglong.database.sp.PanelPreference;
import auto.qinglong.net.NetManager;
import auto.qinglong.ui.BaseFragment;
import auto.qinglong.ui.activity.panel.CodeWebActivity;


public class LogFragment extends BaseFragment {
    public static String TAG = "LogFragment";

    private List<LogFile> logFiles;
    private MenuClickListener menuClickListener;
    private LogAdapter logAdapter;
    private boolean canBack = false;

    private ImageView uiNav;
    private SmartRefreshLayout uiRefresh;
    private TextView uiDir;
    private RecyclerView uiRecycler;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, null);

        uiNav = view.findViewById(R.id.log_nav);
        uiRefresh = view.findViewById(R.id.refresh_layout);
        uiDir = view.findViewById(R.id.log_dir_tip);
        uiRecycler = view.findViewById(R.id.recycler_view);

        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initData();
        }
    }

    @Override
    public boolean onDispatchBackKey() {
        if (canBack && logFiles != null) {
            uiDir.setText(File.pathSeparator);
            logAdapter.setData(logFiles);
            canBack = false;
            return true;
        } else {
            return false;
        }

    }

    public void setMenuClickListener(MenuClickListener mMenuClickListener) {
        this.menuClickListener = mMenuClickListener;
    }

    @Override
    public void init() {
        logAdapter = new LogAdapter(requireContext());
        uiRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        Objects.requireNonNull(uiRecycler.getItemAnimator()).setChangeDuration(0);
        uiRecycler.setAdapter(logAdapter);

        logAdapter.setItemActionListener(file -> {
            if (file.isDir()) {
                canBack = true;
                sortAndSetData(file.getChildren(), file.getTitle());
            } else {
                Intent intent = new Intent(getContext(), CodeWebActivity.class);
                intent.putExtra(CodeWebActivity.EXTRA_TYPE, CodeWebActivity.TYPE_LOG);
                intent.putExtra(CodeWebActivity.EXTRA_TITLE, file.getTitle());
                intent.putExtra(CodeWebActivity.EXTRA_LOG_PATH, file.getParent());
                startActivity(intent);
            }
        });

        uiNav.setOnClickListener(v -> {
            if (menuClickListener != null) {
                menuClickListener.onMenuClick();
            }
        });

        uiRefresh.setOnRefreshListener(refreshLayout -> getLogFiles());
    }

    private void initData() {
        if (initDataFlag || NetManager.isRequesting(getNetRequestID())) {
            return;
        }
        uiRefresh.autoRefreshAnimationOnly();
        new Handler().postDelayed(() -> {
            if (isVisible()) {
                getLogFiles();
            }
        }, 1000);
    }

    @SuppressLint("SetTextI18n")
    private void sortAndSetData(List<LogFile> data, String dir) {
        Collections.sort(data);
        logAdapter.setData(data);
        uiDir.setText(File.pathSeparator + dir);
    }

    private void getLogFiles() {
        auto.qinglong.net.panel.ApiController.getLogFiles(PanelPreference.getBaseUrl(), PanelPreference.getAuthorization(), new auto.qinglong.net.panel.ApiController.LogFileCallBack() {
            @Override
            public void onSuccess(List<LogFile> files) {
                sortAndSetData(files, "");
                logFiles = files;
                canBack = false;
                initDataFlag = true;
                this.onEnd(true);
            }

            @Override
            public void onFailure(String msg) {
                ToastUnit.showShort("加载失败：" + msg);
                this.onEnd(false);
            }

            private void onEnd(boolean isSuccess) {
                if (uiRefresh.isRefreshing()) {
                    uiRefresh.finishRefresh(isSuccess);
                }
            }
        });
    }


}