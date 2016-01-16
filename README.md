# Gunter
Android app that categorizes YouTube gaming videos.

<img src="https://github.com/Grotke/screenshots/blob/master/v0.1.1channellist.png" width="300">
<img src="https://github.com/Grotke/screenshots/blob/master/v0.1.1galm_unexpanded.png" width="300">
<img src="https://github.com/Grotke/screenshots/blob/master/v0.1.1galm_expanded.png" width="300">

## Description and Background
This project is unfinished and won't be developed any further.

This app, codename Gunter, groups YouTube gaming videos by series as indicated by title. 
For example, a channel with a simple naming scheme might name videos like so:
 ```
 WATERMELON LIFE (GMOD MURDER)
 CAT BRAINS (GMOD HIDE N SEEK)
 MOTHER LOVER (GMOD MURDER)
 ```
As you can see, there's some episode unique (usually) flavor text ("WATERMELON LIFE") and a different section indicating the game (GMOD MURDER).
So the first and last video would be grouped together since they're part of the same series while the second video would be in its own series.
 
This app determines which section of the title is relevant for grouping videos together into a series.
 
This app was created to deal with the fact that there's no way to ignore or follow a series. 

The closest thing is a playlist, which is managed by the channel. 
But some channels don't maintain playlists and others don't group videos in a useful way. They may group all three videos under GMOD even though they represent two different game modes.
Playlists also don't allow you to only be notified when a video is added to a certain playlist. Plus in order to get notified, you'd have to be subscribed to the channel.
 
If you're subscribed to a channel that has 4 or 5 series with irregular upload frequencies, but you only like 1 or 2 series, you'd be notified of every video even though most aren't of interest to you. 
This app was intended to allow you to follow someone and some of their series without officially subscribing to them.

## Issues
At the time of abandonment, videos are grouped mostly correctly. By that I mean most channels are grouped almost perfectly while a few channels have a lot of weird problems. This is mostly due to those weird channels having unusual or inconsistent naming conventions.

Pulling videos is quite slow even with just 18 channels, maybe 30 seconds or so. There's no progress bar or anything either. That was to be fixed later.

There's no database and while it can succesfully get subscribed channels from a signed in user, it doesn't do anything with them.
Videos were also supposed to be viewed within the app. It just works with titles right now.

## Building Instructions
If you wanted to build this project for whatever reason, I imagine you'd have to import it to Android Studio since that's what I developed it in. You might be able to use Eclipse too.

First thing you'd need is your own developer key for the YouTube API. Then you'd put it in a public string field called `DEVELOPER_KEY` in a class called `DeveloperKey`.

In the MainActivity's `onCreate()`, the app can be toggled between pulling videos from a hardcoded list of channels and pulling a user's channel subscriptions by commenting out the `launchSubscriptionPull()` or `launchVideoPull()`.

I imagine that's all you'd need to get this project to work, leaving room for inevitable conflicts and compilation errors that mysteriously disappear the next day.
