# activity debug launcher
## Future
* start/debug any activity directly 
* support pass params
* friendlly,you only need to dependency a lib with **debugRuntimeOnly** and install a plugin 

## How to use
* install this plugin:ActivityLauncher
* in your app build.gradle file , add the anchor lib dependency with debugRuntimeOnly, like this:
debugRuntimeOnly 
'io.xnc.intellij.plugin:launchanchor:1.0.2'
* sync the gradle and install debug variant on your debug device
* configure the activity route that you want launch.Enjoy your debugging!
