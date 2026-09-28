# voltify
Simple battery monitor app that tracks battery levels and notifies the user upon reaching a user-defined threshold

## Specification
There will be single screen, which will have settings related inputs, which are as follows
1. Slider for selecting battery level threshold. If device battery level
meets this threshold app will notify user by voice.

2. Text field. Text written in this field will be transformed into voice as announcement
when charge level will satisfy threshold

3. Language picker. The language the app will make announcement when battery level will satisfy
threshold.

4. Delay with fixed set of value e.g 1, 2, 5, 10 minutes. After each of this minutes the app will keep announcing.
5. A full screen notification with fullScreenIntent intent only for battery level reached event.


## Development plan
We are going to write plugin-package for different platform. Primarily for android and iOS and mac for later.
As iOS won't let anything run in background indefinitely, we have to use manual automation feature of iOS.
Here is a synopsys of how to do it

1. Tap New Blank Automation or search for actions.
2. In the search bar, type Show Notification or Speak Text (if you want Siri to read out an alert like "Battery is at 80 percent, unplug your phone!").
3. If you want it to open your custom app, search for the Open App action, tap it, and select your app from your installed list.
4. Tap Done at the top right to save your automation.

***Note***: Its not required for iOS to make such app that will announce the battery level by voice cause Its already available in built in way. Here is a video link [https://www.youtube.com/watch?v=Fkn0OQcRslU].

We will focus on android now.

## Android Development Plan.
We will develop a android plugin. The plugin will do following things:-
1. It will detect whether the charger is plugged in or not.
2. If charger is plugged in, It will start listening on charge level changes.
3. If charge level reach the user given threshold, 
    it will wake up the app like an alarm app, 
    and keep announcing with the user given text. 
    User will be able to stop or snooz that alarm.

## Task plan
1. Start a native android kotlin app.
2. Integrate charger plugged in plugged out listener.
3. Try to capture battery level change event.
4. Try to capture battery level change event from background
5. Try to throw full screen notification.
6. After all these steps done successfully, we will be port the code
   to flutter android package plugin. 





