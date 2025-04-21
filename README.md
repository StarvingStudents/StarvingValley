# StarvingValley

## Setup

### Firebase config
1. Download `google-services.json` file from [here](https://console.firebase.google.com/u/0/project/starving-valley/settings/general/android:io.github.StarvingValley)
2. Place it into the `/android` folder


## Deployment
We created a Makefile for taking the friction out of building and deploying. To use it: 
1. Make sure your ANDROID_HOME environment variable points to your SDK
2. Run `make all` to build the game
3. Run `make install-apk` to install the game on your android emulator.
4. In the future once you will also be able to deploy the app to Play Store directly by running `make publish-apk`
 

## Credits

Assets from: Sprout Lands by Cup Nooble
https://cupnooble.itch.io/sprout-lands-asset-pack