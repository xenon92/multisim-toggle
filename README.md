MultiSIM Toggle
===

**Read more about the app [HERE](http://blog.shubhangrathore.com/multisim-toggle/index.html)**

An open source app for android, to enable or disable multi-SIM functionality on multi-SIM supporting AOSP based ROMs. The app toggles the ROM from multi-SIM to single SIM functionality and vice versa.

During my debugging work, I found myself switching from single SIM mode to multi-SIM and vice versa quite frequently. To do so, I had to type the following shell commands:

- Allow SuperUser execution at shell
> su

- To enable multi-SIM
> setprop persist.radio.multisim.config dsds

- To disable multi-SIM
> setprop persist.radio.multisim.config none

So I made an app for that. It executes these commands at shell level without the need to open terminal manually and type in these commands.

**NOTE:** Use this app only if you are sure that your **ROM** supports multi-SIM. Even if your device is multi-SIM capable, but your ROM isn't, using this app might lead to unforeseeable results. Contact your device maintainer to add multi-SIM support.


Features
---

- Minimal and clean interface
- Translucent decors for android 4.4+ (kitkat)
- Android L style floating buttons (thanx to **[FAB](https://github.com/FaizMalkani/FloatingActionButton)** library)

Screenshots
---

![](http://xenon92.github.io/blog/content/images/2014/Jul/Screenshot_2014-07-28-16-34-11_pwonexus520140728_163656.png?raw=true)

![](http://xenon92.github.io/blog/content/images/2014/Jul/Screenshot_2014-07-28-16-34-18_ngtnexus520140728_163636.png?raw=true)

![](http://xenon92.github.io/blog/content/images/2014/Jul/Screenshot_2014-07-28-16-31-31_kdpnexus520140728_163722.png)

How to use?
---

- Take a nandroid backup
- Install **[MultiSIM Toggle](https://github.com/xenon92/multisim-toggle/releases)**
- Tap the floating SIM button
- Grant SuperUser permission
- Automatic reboot of the device if toggle is successful

Support
---

Bugs reports, suggestions and support can be found on XDA-developers **[thread](http://forum.xda-developers.com/android/apps-games/app-multisim-toggle-t2830647)** for MultiSIM Toggle.


Requirements
---

- Root
- Android 4.0.3+
- MultiSIM supporting AOSP based ROM


Downloads
---

Check the '**[Releases](https://github.com/xenon92/multisim-toggle/releases)**' section in my Github source.


Changelog
---

View the '**[Changelog](https://github.com/xenon92/multisim-toggle/blob/master/CHANGELOG.md)**' on my Github.


License
---

The source code is licensed under GNU General Public License v3 (**[GPL v3](https://github.com/xenon92/multisim-toggle/blob/master/LICENSE)**)


Copyright
---

&copy; 2014 Shubhang Rathore
