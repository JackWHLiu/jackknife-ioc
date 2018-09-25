JackKnife-IOC帮助文档![Release](https://jitpack.io/v/JackWHLiu/jackknife-ioc.svg)  [![API](https://img.shields.io/badge/API-11%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=11)
================================
![avatar](http://jackwhliu.cn/images/banner3.jpg)

一、环境配置
--------------------------------
在gradle中配置环境。
#### //指定仓库的地址，在project的build.gradle加入粗体字的代码。
<blockquote>
allprojects {
  repositories {
    jcenter()
    <h3>maven { url "https://jitpack.io" }</h3>
  }
}
</blockquote>

#### //依赖本库，在app模块的build.gradle加入加粗的代码，版本号也可改成master-SNAPSHOT直接拿最新代码编译。
<blockquote>
dependencies {
    <h3>compile 'com.github.JackWHLiu:jackknife-ioc:2.6.2'</h3>
}
</blockquote>

二、如何使用(参考https://github.com/JackWHLiu/JackKnifeDemo)
--------------------------------
### (一)基于IOC依赖注入的自动注入视图、绑定控件和注册事件（jackknife-ioc）
#### 1、自动注入视图（Inject Layout）
##### （1）Activity继承com.lwh.jackknife.app.Activity,Fragment继承com.lwh.jackknife.app.Fragment
##### （2）保证布局的xml文件和Activity和Fragment的Java类的命名遵循一定的映射关系（Java类名必须以Activity或Fragment结尾）。
<blockquote>
    <b>前缀+名字，如activity_main</b>
    例如：MainActivity.java映射的xml文件名就为activity_main.xml，TTSFragment.java映射的xml文件名就为fragment_t_t_s.xml。
    Java文件以大写字母分隔单词，xml以下划线分隔单词。
</blockquote>
 
#### 2、自动绑定控件（Inject Views）
##### （1）不使用注解
> 直接在Activity或Fragment声明控件（View及其子类）为成员变量，不加任何注解。它会以这个View的名字来绑定该控件在xml中的id的value，即@+id/后指定的内容。
##### （2）使用@ViewInject
> 优先级比不加注解高，简单的说，加上这个注解就不会使用默认的使用成员属性名来对应xml的控件id的方式，而是使用该注解指定的id与xml的控件id绑定。
##### （3）使用@ViewIgnore
> 优先级最高，加上该注解，jackknife会直接跳过该控件的自动注入。一般使用在使用Java代码new出来的控件提取到全局的情况。也可以在ViewStub懒加载布局的时候使用。
#### 3、自动注册事件（Inject Events）
>  ）创建一个自定义的事件注解，在这个注解上配置@EventBase，并使用在你要实际回调的方法上，<b>注意保持参数列表跟原接口的某个回调方法的参数列表保持一致</b>。在jackknife-annotations-ioc中也提供了常用的事件的注解，比如@OnClick。

二、博客（绿色通道）
--------------------------------
简书 : https://www.jianshu.com/u/8f43e6fd56db, https://www.jianshu.com/u/f408bdadacce
CSDN : http://blog.csdn.net/yiranaini_/