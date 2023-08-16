# Contributing Guidelines

## Filing a Bug Report

Simply create a new issue for a bug, and one of us at the VicRoy team will check it out!

In your bug report please include the following 
* A summary of what the bug is
* The observed and expected behaviour
* Steps to reproduce the bug
* Other contextual information (e.g. version and operating environment)
* Screenshots would also be great if possible

## Suggesting a New Feature

We have created a roadmap for the project and what features we would like to add as the project progresses. However, we are always open to new ideas and cool 
suggestions so if you do have a lightbulb moment, feel free to send us an email at vroyale374@gmail.com telling us all about it!

## Submitting Up a Pull Request

From your local copy of RondayView, make sure all of the changes you make are on a new branch!

After you are happy with all of your changes, do a git push from your branch to github. From your forked repo go into "Pull requests" in the navbar and click on the green "New pull request" button.

Select the branch that you want to create the pull request for and GitHub will let you know if there are any merge conflicts. If there are merge conflicts, it will give you some suggestions on how to solve them. Once it is able to merge, you can click the "Create pull request" button. 

Write a short paragraph describing what you have done in the pull request and which issue it addresses.

From there, one of out team members here at VicRoy will review your pull requestand give you some constructive feedback. Once it all looks good to go, we would be honoured to merge your contribution!

## How to Set Up Your Environment and Run Tests

All you need is Android Studio installed and set up!

Follow this link to download Android Studio: https://developer.android.com/studio

And watch this video to see how to set up Android Studio: https://www.youtube.com/results?sp=mAEB&search_query=how+to+set+up+android+studio

Once you have opened the project, you can run it by clicking the green triangular play button on the top right of the screen, and the app will open up in your android emulator. Alternitavely, if you have an android phone, you can run the app on your physical device by following these instructions: https://developer.android.com/studio/run/device

## Technical Requirements for Contributions

Our backend logic is all in java so please use java and not kotlin!

*****CHANGE THIS PERHAPS
We also value having a consistent style in our program, this is to minimise confusion and keep everything looking neat and consistent! As such we will be following
googles style guide for java programs shown at this link: https://google.github.io/styleguide/javaguide.html

Also, our branch documentation style is as follows

    Start the branch with one of these tags 

        feat - for feature
        fix - for bug fix
        test - for testing additions
        docs - documentation update

    Then add a /

    Then a relevant title for your branch all in lower case with words seperated by dashes -

Here is an example of a branch name: feat/creating-home-UI

## Our Vision

We have put a lot of thought into how we want RondayView to move forward with features, improvements and awesomeness from start to finish with some checkpoints 
in between. We have split the app up into 3 stages and called them A1, A2 and A3, with each stage building upon the former. As such, we would like all features
of A1 to be completed before starting features of A2, and the same for A2 going to A3. I have listed these milestone features below but they are also shown in 
the Issues tab. 

For A1, we would like to cover basic functions of the app. These include the following:
* Create Events – anyone can create events initially and this is stored in database
* Browse Events – anyone can browse events
* Swipe events to show interest or disinterest
* Basic Filter - event dates, event size, virtual or in person
* Basic initial user interface
* Profiles - Local storage / Favourites

Moving to A2, we would like to build mostly upon the social aspects of the program. The features that we would like to implement are as follows:
* User profiles - Sign in, accounts stored in databases
* Able to add other user profiles as friends
* View events that friends are going to
* View popular events among all users
* Leaderboard aspect (who has visited the most events)

Finally, at A3, we would like to add features that integrate the project into other aspects of the phone, as well as further cater the app towards individual users with the following features:
* In-app and push notifications for upcoming events
* Link to the phones calendar
* Advanced Filters - Tags / event type, event host name
* Each event gets a photo album that attendees can add to
* Event rating and comments – previous events rating too
* Suggested similar events in event description
* Private Groups and private events

All of these features are detailed more in the GitHub Issues tab so you can check them out there too. Feel free to send us an email if you would like to contribute and have any questions about a feature at vroyale374@gmail.com

## High Level Design/Architecture Information

Class Diagram
![Class Diagram](contributingIMGS/classDiagram.png)

$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$4
Deveshs Database Diagram
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$4

## Project Ground Rules

We at VicRoy strongly support inclusivity for all peoples as well as fostering a harrasment and bully free environment. This means that comments on pull requests should be constructive, helpful and kind. We ask that you avoid condecending words such as "obvious", "easy", or "simply" and avoid making comments personal. Please keep in mind that we are all at different stages in our journey and are all learning. Respect is important to us, so please treat eachother as you would like to be treated.

For more information on our policies and ground rules, have a look at out [Code of Conduct](CODE_OF_CONDUCT).

## How to Get in Touch

For any futher questions, comments or suggestions, please feel free to email us at vroyale374@gmail.com

## Closing Statements

Thank you for choosing to contribute to our project! We are so glad you could be a part of RondayView and are as passionate about it as we are!





