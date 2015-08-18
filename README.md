## DrawerLayoutDemo

*	In development,in order to implement vertical bar/sidebar navigation,
open source library of Slidemenu is normally utilized. 
An existing problem is that slidemenu navigation cannot be retrived by moving scroll bar,
unless by triggering "Click on", when both Viewpager and Slidemenu appear at the sametime,
and also when position of Viewpager current view position > 0.


## setup
* In Eclipse, just import the library as an Android library project. Project > Clean to generate the binaries you 	need, like R.java, etc.

* Then, just add StarDream as a dependency to your existing project and you're good to go!

## Setup with actionbarsherlock
* Setup as above.
* Checkout a clean copy of ActionBarSherlock and import into your Eclipse workspace.
* Add ActionBarSherlock as a dependency to StarDream
* Go into the StarDream that you plan on using make them extend Sherlock___Activity instead of ___Activity.

## How to Integrate this Library into Your Projects

## Xml Useage
![icon](example.jpg)

[@官网API](http://developer.android.com/reference/android/support/v4/widget/DrawerLayout.html)

##Caveats
* Your layouts have to be based on a viewgroup

## Developed By
 * longmingzhang
