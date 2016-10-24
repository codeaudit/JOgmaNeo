<!---
  JOgmaNeo
  Copyright(c) 2016 Ogma Intelligent Systems Corp. All rights reserved.

  This copy of JOgmaNeo is licensed to you under the terms described
  in the JOGMANEO_LICENSE.md file included in this distribution.
--->

# Java JNI bindings for OgmaNeo

[![Build Status](https://travis-ci.org/ogmacorp/JOgmaNeo.svg?branch=master)](https://travis-ci.org/ogmacorp/JOgmaNeo)

## Introduction

JOgmaNeo contains Java JNI bindings to the main [OgmaNeo](https://github.com/ogmacorp/OgmaNeo) C++ library.

These bindings provide an interface into the C++ library. Allowing Java code to gain access to the OgmaNeo CPU and GPU accelerated algorithms. Implementation(s) of Online Predictive Hierarchies, as described in arXiv.org paper: [Feynman Machine: The Universal Dynamical Systems Computer](http://arxiv.org/abs/1609.03971).

## Requirements

The same requirements that OgmaNeo has, are required for JOgmaNeo: a C++1x compiler, [CMake](https://cmake.org/), and an OpenCL SDK.

Additionally JOgmaNeo requires installation a Java SDK, [SWIG](http://www.swig.org/), and the Khronos `cl2.hpp` header file.

#### [SWIG](http://www.swig.org/)

- Linux requires SWIG installed via, for example, ```sudo apt-get install swig``` command (or via ```yum```).
- Windows requires installation of SWIG (2.0.1+). With the SourceForge Zip expanded, and the PATH environment variable updating to include the SWIG installation binary directory (for example `C:\Program Files (x86)\swigwin-3.0.8`).

#### [cl2.hpp](http://github.khronos.org/OpenCL-CLHPP/)

The `cl2.hpp` header file can be downloaded from Github https://github.com/KhronosGroup/OpenCL-CLHPP/releases and needs to be placed alongside your OpenCL header files.

## Installation

Once the JOgmaNeo requirements have been setup. The following can be used to build the JOgmaNeo Java archive and library:

> mkdir build; cd build  
> cmake ..  
> make  

This will create a `JOgmaNeo.jar` archive file and an associated shared library (on Windows `JOgmaNeo.dll`, Linux `libJOgmaNeo.so`, Mac OSX `libJOgmaNeo.jnilib`).

JOgmaNeo has been tested on Windows, Mac OSX, and Linux using the Oracle SE JDK v1.8.0_112

## Importing and Setup

The following example Java code can be found in the `src/com/ogmacorp/Example.java` file.

The JOgmaNeo module can be imported into Java code using:

```java
import com.ogmacorp.ogmaneo.*;
```

Two interfaces are used to setup JOgmaNeo:
- The `ComputeSystemInterface` is used to setup the OgmaNeo C++ library and OpenCL.
- The `ComputeProgramInterface` is used to load default packaged OpenCL kernel code.

For example:
```java
ComputeSystemInterface _csi = new ComputeSystemInterface();
ComputeProgramInterface _cpi = new ComputeProgramInterface();

_csi.create(ComputeSystem.DeviceType._gpu);
_cpi.loadMainKernel(_csi);
```

Layer properties can be accessed via `LayerDescs`. With the main hierarchy accessed through `Hierarchy`. For example:
```java
LayerDescs[] layers = new LayerDescs[2];
vectorld layer_descs = new vectorld();

for (LayerDescs layer : layers) {
    layer = new LayerDescs();
    layer.set_width(256);
    layer.set_height(256);
    layer_descs.add(layer);
}

int w = 16;
int h = 16;

Hierarchy hierarchy = new Hierarchy(_csi.get(), _cpi.get(), w, h, layer_descs, -0.01f, 0.01f, 1337);
```

`Hierarchy.simStep` is used to pass input into the hierarchy and run through one prediction step. In the following `input` is a `vectorf` array type of 16*16 values. For example:
```java
hierarchy.simStep(input, true);
```

`Hierarchy.getPrediction()` is used to obtain prediction after a `simStep` has taken place.
```java
vectorf prediction = hierarchy.getPrediction();
```

A hierarchy can be saved and loaded as follows:
```java
hierarchy.save(_csi.get(), "filename.opr");
hierarchy.load(_csi.get(), _cpi.get(), "filename.opr");
```

## OgmaNeo Developers

By default JOgmaNeo uses the `CMake/FindOgmaNeo.cmake` script to find an existing installation of the OgmaNeo library. If it cannot find the library, CMake automatically clones the OgmaNeo master repository and builds the library in place.

Two options exist for OgmaNeo library developers that can redirect this process:

- The `CMakeLists.txt` file can be modified locally to point to a fork of an OgmaNeo repository, and also clone a particular branch from a fork. The `GIT_REPOSITORY` line in `CMakeLists.txt` file can be changed to point to a fork location. An additional `GIT_TAG` line can be added to obtain a particular branch from a fork.

- If you require JOgmaNeo to use a local clone of OgmaNeo, two CMake arguments can be used OGMANEO_LIBRARY and OGMANEO_INCLUDE_DIR.  
Change `<repo_dir>` to point to your OgmanNeo root directory, or to appropriate system wide locations. This following assumes that the OgmaNeo CMAKE_INSTALL_PREFIX has been set to `<repo_dir>/install` and that a `make install` build step has been performed.
  > cmake -DOGMANEO_LIBRARY=\<repo_dir\>/install/lib/OgmaNeo.lib -DOGMANEO_INCLUDE_DIR=\<repo_dir\>/install/include ..

## Contributions

Refer to the JOgmaNeo [CONTRIBUTING.md](https://github.com/ogmacorp/JOgmaNeo/blob/master/CONTRIBUTING.md) file for details about contributing to JOgmaNeo. The same instructions apply for contributing to OgmaNeo, including the signing of the [Ogma Contributor Agreement](https://ogma.ai/wp-content/uploads/2016/09/OgmaContributorAgreement.pdf).

## License and Copyright

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />The work in this repository is licensed under the <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>. See the [JOGMANEO_LICENSE.md](https://github.com/ogmacorp/JOgmaNeo/blob/master/JOGMANEO_LICENSE.md) and [LICENSE.md](https://github.com/ogmacorp/JOgmaNeo/blob/master/LICENSE.md) file for further information.

Contact Ogma Intelligent Systems Corp licenses@ogmacorp.com to discuss commercial use and licensing options.

JOgmanNeo Copyright (c) 2016 [Ogma Intelligent Systems Corp](https://ogmacorp.com). All rights reserved.
