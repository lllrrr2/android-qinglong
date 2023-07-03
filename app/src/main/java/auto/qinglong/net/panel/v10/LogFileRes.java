package auto.qinglong.net.panel.v10;

import java.util.List;

import auto.qinglong.net.panel.BaseRes;

/**
 * @author wsfsp4
 * @version 2023.07.03
 */
public class LogFileRes extends BaseRes {
    private List<LogFileObject> dirs;

    public List<LogFileObject> getDirs() {
        return dirs;
    }

    public void setDirs(List<LogFileObject> dirs) {
        this.dirs = dirs;
    }

    public static class LogFileObject {
        private boolean isDir;
        private String name;
        private List<String> files;

        public boolean isDir() {
            return isDir;
        }

        public void setDir(boolean dir) {
            isDir = dir;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getFiles() {
            return files;
        }

        public void setFiles(List<String> files) {
            this.files = files;
        }
    }
}