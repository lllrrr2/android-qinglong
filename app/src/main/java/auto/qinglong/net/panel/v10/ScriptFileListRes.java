package auto.qinglong.net.panel.v10;

import java.util.List;

import auto.qinglong.net.panel.BaseRes;

/**
 * @author wsfsp4
 * @version 2023.07.03
 */
public class ScriptFileListRes extends BaseRes {
    private List<FileObject> data;

    public List<FileObject> getData() {
        return data;
    }

    public void setData(List<FileObject> data) {
        this.data = data;
    }

    public static class FileObject {
        private double mtime;
        private String title;
        private List<FileObject> children;

        public boolean isDir() {
            return children != null;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<FileObject> getChildren() {
            return children;
        }

        public void setChildren(List<FileObject> children) {
            this.children = children;
        }

        public double getMtime() {
            return mtime;
        }

        public void setMtime(double mtime) {
            this.mtime = mtime;
        }
    }
}
