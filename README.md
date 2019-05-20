[![](https://jitpack.io/v/DmytroDmytryshyn/PermissionLibrary.svg)](https://jitpack.io/#DmytroDmytryshyn/PermissionLibrary)
# PermissionLibrary

An a lightweight permissions request library.

# Usage

Step 1. Add it in your root build.gradle at the end of repositories:

```xml
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
  
Step 2. Add the dependency:

```xml
dependencies {
    implementation 'com.github.DmytroDmytryshyn:PermissionLibrary:1.1'
}
```

Step 3. Implement "IPermissionResultHandler" interface in activity or fragment:

```xml
class MainActivity : AppCompatActivity(), IPermissionResultHandler {

    ...
    
    override fun onPermissionsGranted() {
    }

    override fun showRationale(reGrantPermissions: () -> Unit) {
    }

    override fun onDoNotAskAgainChecked() {
    }
}
```

Step 4. Handle onRequestPermissionsResult:

```xml
    ...
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permission.onRequestPermissionsResult(
            permissionResultHandler = this,
            requestCode = requestCode,
            grantResults = grantResults
        )
    }
```

Step 5. Request permission(s):

```xml
    ...
     permission.grantPermissions(
                permissionResultHandler = this,
                permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                permissionsRequestCode = 0x1
     )
```

### MIT License

```
    The MIT License (MIT)
    
    Copyright (c) 2014 Junguan Zhu
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
```
