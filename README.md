# bitCraft Mod

### Contributing

Found a bug? Have a great idea you think would fit well in the mod? Awesome, we'd love to work with you!

#### Submitting a PR

Here's an overview of how to submit a code change:

1. Fork the repo. Click the `Fork` icon located at the top-right of this page (below your username).
2. Modify the code. Make the changes that you want to and commit them.
3. Click `Pull Request` at the right-hand side of the gray bar directly below your fork's name.
4. Click `Click to create a pull request for this comparison`, enter your PR's title, and create a detailed description telling us what you changed.
5. Click `Send pull request`, and wait for feedback!

#### Creating an Issue

If you find a bug or issue, here's how to report it:

1. Make sure your issue hasn't already been answered or fixed by [searching for it](https://github.com/littlebits/bitCraft/search?q=&type=Issues).
2. Go to [the issues page](http://github.com/littlebits/bitCraft/issues).
3. Click `New Issue` right below `Star` and `Fork`.
4. Enter your Issue's title (something that summarizes your issue), and then create a detailed description.
	* If you are reporting a bug, make sure you include the following:
		* Server log if applicable
		* Detailed description of the bug and pictures if applicable
5. Click `Submit new issue`, and wait for feedback!

### Setting Up the Development Environment

- Make sure you have [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) installed.
- Clone your fork repo and make sure your have the latest changes available here (_upstream_).
- Extract the contents of `forge/forge-1.7.10-10.13.3.1388-1.7.10-src.zip` into the folder `code`. Do not overwrite the `code/src` or `code/build.gradle` files.
- From the `code` folder, run the command:

	- For Windows:
		`gradlew setupDecompWorkspace --refresh-dependencies`
	- For Linux/Mac OS:
		`./gradlew setupDecompWorkspace --refresh-dependencies`
		If this doesn't work, run `chmod +x gradlew` and retry the above command.

- If you run `eclipse`, all you have to do now is run

	- For Windows:
		`gradlew eclipse`
	- For Linux/Mac OS
		`./gradlew eclipse`
		If this doesn't work, run `chmod +x gradlew` and retry the above command.

- If you run IDEA IntelliJ, the steps are a little different.

	- Open IDEA IntelliJ
	- Select `Import project`
	- Navigate to your workspace and select the `build.gradle` file. Once IDEA IntelliJ finishes importing the project, exit the IDE
	- In your command window, run:

		- For Windows
			`gradlew genIntellijRuns`
		- For Linux/Mac OS
			`./gradlew genIntellijruns`
			If this doesn't work, run `chmod +x gradlew` and retry the above command.

### License
Copyright 2015 littleBits Electronics.

littleBits, littleBits logo, Bits, cloudBit, are trademarks of littleBits Electronic Inc.

littleBits Electronics, Inc. supports the Open Source Hardware Statement of Principles by making the circuit designs for its modules available pursuant to the CERN Hardware License, Version 1.2.

This is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License at http://www.gnu.org/licenses/ for more details.

### Modpack Inclusion
Want to use bitCraft in a mod pack? Go right ahead! bitCraft is released under the [GPL v3 license](http://www.gnu.org/licenses/gpl-3.0.en.html), so you're legally granted the rights to redistribute this mods for as long as you retain the license information and let players know where they can find the mod's source.

If you do choose to include it in a modpack, please tell us, we're curious!
