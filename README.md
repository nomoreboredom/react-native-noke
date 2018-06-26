
# react-native-noke

## Getting started

`$ npm install react-native-noke --save`

### Mostly automatic installation

`$ react-native link react-native-noke`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-noke` and add `RNNoke.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNNoke.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNNokePackage;` to the imports at the top of the file
  - Add `new RNNokePackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-noke'
  	project(':react-native-noke').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-noke/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-noke')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNNoke.sln` in `node_modules/react-native-noke/windows/RNNoke.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Noke.RNNoke;` to the usings at the top of the file
  - Add `new RNNokePackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNNoke from 'react-native-noke';

// TODO: What to do with the module?
RNNoke;
```
  