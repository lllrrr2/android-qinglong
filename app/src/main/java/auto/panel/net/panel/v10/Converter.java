package auto.panel.net.panel.v10;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import auto.base.util.TimeUnit;
import auto.panel.bean.panel.Dependence;
import auto.panel.bean.panel.Environment;
import auto.panel.bean.panel.File;
import auto.panel.bean.panel.SystemConfig;
import auto.panel.bean.panel.Task;
import auto.panel.utils.CronUnit;

/**
 * @author wsfsp4
 * @version 2023.06.29
 */
public class Converter {
    public static List<Task> convertTasks(List<TasksRes.TaskObject> objects) {
        List<Task> result = new ArrayList<>();
        if (objects == null || objects.isEmpty()) {
            return result;
        }

        try {
            for (TasksRes.TaskObject object : objects) {
                Task task = new Task();
                task.setKey(object.getId());
                task.setName(object.getName());
                task.setPinned(object.getIsPinned() == 1);
                task.setCommand(object.getCommand());
                task.setSchedule(object.getSchedule());
                //任务上次执行时间
                if (object.getLastExecutionTime() > 0) {
                    task.setLastExecuteTime(TimeUnit.formatDatetimeA(object.getLastExecutionTime() * 1000));
                } else {
                    task.setLastExecuteTime("--");
                }
                //任务上次执行时长
                if (object.getLastRunningTime() >= 60) {
                    task.setLastRunningTime(String.format(Locale.CHINA, "%d分%d秒", object.getLastRunningTime() / 60, object.getLastRunningTime() % 60));
                } else if (object.getLastRunningTime() > 0) {
                    task.setLastRunningTime(String.format(Locale.CHINA, "%d秒", object.getLastRunningTime()));
                } else {
                    task.setLastRunningTime("--");
                }
                //任务下次执行时间
                task.setNextExecuteTime(CronUnit.nextExecutionTime(object.getSchedule(), "--"));
                // 任务状态
                if (object.getStatus() == 0) {
                    task.setState("运行中");
                    task.setStateCode(Task.STATE_RUNNING);
                } else if (object.getStatus() == 3) {
                    task.setState("等待中");
                    task.setStateCode(Task.STATE_WAITING);
                } else if (object.getIsDisabled() == 1) {
                    task.setState("已禁止");
                    task.setStateCode(Task.STATE_LIMIT);
                } else {
                    task.setState("空闲中");
                    task.setStateCode(Task.STATE_FREE);
                }
                result.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<Environment> convertEnvironments(List<EnvironmentsRes.EnvironmentObject> objects) {
        List<Environment> result = new ArrayList<>();
        if (objects == null || objects.isEmpty()) {
            return result;
        }

        try {
            for (EnvironmentsRes.EnvironmentObject object : objects) {
                Environment environment = new Environment();
                environment.setKey(object.getId());
                environment.setName(object.getName());
                environment.setValue(object.getValue());
                environment.setRemark(object.getRemarks());
                environment.setPosition(object.getPosition());
                environment.setStatusCode(object.getStatus());
                environment.setTime(TimeUnit.formatDatetimeA(object.getCreated()));
                result.add(environment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<File> convertLogFiles(List<LogFilesRes.FileObject> objects) {
        List<File> result = new ArrayList<>();
        if (objects == null || objects.isEmpty()) {
            return result;
        }

        try {
            for (LogFilesRes.FileObject object : objects) {
                File logFile = new File();
                logFile.setTitle(object.getName());
                logFile.setDir(object.isDir());
                logFile.setParent("");
                logFile.setPath(object.getName());

                if (object.isDir()) {
                    List<File> children = new ArrayList<>();
                    for (String name : object.getFiles()) {
                        File child = new File();
                        child.setDir(false);
                        child.setTitle(name);
                        child.setParent(object.getName());
                        child.setPath(object.getName() + "/" + name);
                        children.add(child);
                    }
                    logFile.setChildren(children);
                }
                result.add(logFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<Dependence> convertDependencies(List<DependenciesRes.DependenceObject> objects) {
        List<Dependence> result = new ArrayList<>();
        if (objects == null || objects.isEmpty()) {
            return result;
        }

        try {
            for (DependenciesRes.DependenceObject object : objects) {
                Dependence dependence = new Dependence();
                dependence.setKey(object.getId());
                dependence.setTitle(object.getName());
                dependence.setCreateTime(TimeUnit.formatDatetimeA(object.getCreated()));
                dependence.setStatusCode(object.getStatus());
                result.add(dependence);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<File> convertScriptFiles(List<ScriptFilesRes.FileObject> objects) {
        List<File> result = new ArrayList<>();
        if (objects == null || objects.isEmpty()) {
            return result;
        }

        try {
            for (ScriptFilesRes.FileObject object : objects) {
                File file = new File();
                file.setTitle(object.getTitle());
                file.setDir(object.isDir());
                file.setParent("");
                file.setCreateTime(TimeUnit.formatDatetimeA((long) object.getMtime()));
                file.setPath(object.getTitle());

                if (object.isDir()) {
                    file.setChildren(buildChildren(file.getPath(), object.getChildren()));
                }

                result.add(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static SystemConfig convertSystemConfig(SystemConfigRes.SystemConfigObject object) {
        SystemConfig config = new SystemConfig();

        try {
            config.setLogRemoveFrequency(object.getFrequency());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return config;
    }

    private static List<File> buildChildren(String parent, List<ScriptFilesRes.FileObject> objects) {
        List<File> children = new ArrayList<>();
        if (objects == null || objects.isEmpty()) {
            return children;
        }

        try {
            for (ScriptFilesRes.FileObject object : objects) {
                File file = new File();
                file.setTitle(object.getTitle());
                file.setParent(parent);
                file.setDir(object.isDir());
                file.setCreateTime(TimeUnit.formatDatetimeA((long) object.getMtime()));
                file.setPath(parent + "/" + object.getTitle());

                if (object.isDir()) {
                    file.setChildren(buildChildren(file.getPath(), object.getChildren()));
                }

                children.add(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return children;
    }
}
