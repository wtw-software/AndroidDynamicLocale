AndroidDynamicLocale
====================

Fetch language strings from server and store locally

```
<dependency>
  <groupId>no.wtw.android</groupId>
  <artifactId>dynamic-locale</artifactId>
  <type>apklib</type>
  <version>xxx</version>
</dependency>
```

## Usage 

Calls are made statically: ```DynamicLocale._(myContext, R.string.my_string_ref);```

## Updating language

String keys on server must match the ```strings.xml``` reference, eg. ```R.string.my_string_ref``` must have a server string key ```my_string_ref```.

If a server string is not present, default string from ```strings.xml``` will be used.

## Updating app

Before releasing a new version of the app, strings from server should be copied into default values in the app, or else app defaults will divert more and more from the correct strings. 
