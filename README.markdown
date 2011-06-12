osmosis-simplifyways
====================
Way Simplification Plugin for Osmosis
=====================================

Summary
-------

          osmosis --read-xml complicated.osm --simplify-ways epsilonMeters=10 --wx simplified.osm

About
-----

This plugin simplifies the way geometry by removing some intermediate nodes. The underlying simplification/algorithm used is the Ramer-Douglas-Peucker algorithm. A maximum deviation from the original way geometry can be passed as a parameter to control the number of nodes removed. The [JTS library](http://tsusiatsoftware.net/jts/main.html) is used for the geometry computations. This plugin has been tested with Osmosis 0.39.

Building and Installation
-------------------------

Building is quite straightforward. You will need a JDK for Java 6 and Ant 1.7+ installed. Also, for the first build you will need an internet connections so the build system can download the dependencies.

Once you have the dependencies, execute:

          git clone git://github.com/podolsir/osmosis-simplifyways.git
          cd osmosis-simplifyways
          ant dist

After the build completes there should be two jar files in the `dist` subdirectory: `osmosis-simplifyways--...jar` and `jts-1.11.jar`. 
Copy those jar files into your Osmosis classpath (for example, `$OSMOSIS_HOME/lib/default`).

Additionally, there is a zip file containing both of these jar files for simpler distribution. 

Usage
-----

### --simplify-ways (--simw) ###

Simplifies the ways in the given entity stream using the Ramer-Douglas-Peucker algorithm.

#### Pipes ####
_inPipe.0:_ Consumes an entity stream.    
_outPipe.0:_ Produces an entity stream.

#### Parameters ####
_epsilonMeters_: __Optional, default value: 0.1.__ The maximum allowed deviation from the original way geometry, in meters.

#### Notes ####

This task requires the following order in the input stream: **nodes, then ways**. Relation and bound entities are passed through as is and cann occur at any point in the stream.

The output order is: **ways/relations/bounds, then nodes**.

The first and last node of a way are always preserved. 

Licenses
--------

Everybody is granted an irrevocable and perpetual license to use osmosis-simplifyways for any purpose whatsoever.

Note that this does not apply to the JTS library which is licensed under LGPL.

DISCLAIMER:
By making osmosis-simplifyways publicly available, it is hoped that users will find the
software useful.  However:

* osmosis-simplifyways comes without any warranty, to the extent permitted by applicable
law.

* Unless required by applicable law, no liability will be accepted by
the authors and distributors of this software for any damages caused
as a result of its use. 
