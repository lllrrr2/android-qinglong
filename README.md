# 定时精灵

## 软件介绍

本软件基于**青龙面板**接口开发，支持面板大部分原生功能，同时提供拓展模块，帮助用户快捷管理。

## 使用环境

* 安卓版本：8.0+
* 面板版本：参考[APP版本](https://gitee.com/wsfsp4/public-static-file/blob/master/qinglong/version.md)，选择合适软件版本

## 代码说明

本仓库代码截止*2.0.5*版本，后续版本代码将不再同步更新至仓库，只进行apk包发布。

## 功能介绍

### 用户登录

支持通过域名和IP地址形式登录，默认为HTTP协议，支持以下地址格式：

* 127.0.0.1:5700
* www.example.com
* www.example.com:5700
* https://www.example.com
* https://www.example.com:5700

### 基础功能

提供定时任务、环境变量、配置文件、脚本管理、依赖管理、任务日志和系统设置功能。

* 定时任务：支持增改删查、批量操作、查看日志、查看脚本、任务去重、本地备份和本地导入；
* 环境变量：支持增改删查、批量操作、变量去重、快捷导入、本地备份和本地导入；
* 配置文件：支持查看和修改配置；
* 依赖管理：支持新建、删除、批量操作和查看日志；
* 脚本管理：支持查看、编辑、删除、更新和下载脚本；
* 任务日志：支持查看日志文件列表；
* 系统设置：支持常规设置和登录日志查看；

#### 操作提示

| 模块   | 单击标题 | 长按标题 | 长按内容 |
|------|------|------|------|
| 定时任务 | 查看日志 | 跳转脚本 | 编辑   |
| 环境变量 |      |      | 编辑   |
| 脚本管理 | 查看内容 | 操作栏  | 操作栏  |
| 依赖管理 | 查看日志 |      |      |
| 任务日志 | 查看内容 |      |      |

- 变量快捷导入将从输入文本中提取'***export xx="xxxx"***'格式内容作为一个变量,支持同时提取多个变量；
- 本地导入搜索对应模块备份路径下的json文件，将外部文件复制到该模块路径下即可使用；
- 任务备份路径：Android/data/auto.panel/files/tasks；
- 变量备份路径：Android/data/auto.panel/files/environments；
- 脚本备份路径：Android/data/auto.panel/files/scripts；
- 运行日志路径：Android/data/auto.panel/files/logs；

### 拓展模块

拓展模块将在后续版本逐步开放，具体使用方法请查看相应文档。

## 界面预览

<img src="https://gitee.com/wsfsp4/public-static-file/raw/master/qinglong/p1.jpg" alt="登录页面" width="22%"> 
<img src="https://gitee.com/wsfsp4/public-static-file/raw/master/qinglong/p2.jpg" alt="模块导航" width="22%">  
<img src="https://gitee.com/wsfsp4/public-static-file/raw/master/qinglong/p3.jpg" alt="代码编辑" width="22%">  
<img src="https://gitee.com/wsfsp4/public-static-file/raw/master/qinglong/p4.jpg" alt="系统设置" width="22%">  

## 下载地址

[历史版本](https://gitee.com/wsfsp4/QingLong/releases)

## 相关项目

* [qinglong](https://github.com/whyour/qinglong)

## 交流反馈

出现Bug或者有功能需求请创建Issue！
