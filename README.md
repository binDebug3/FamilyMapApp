# FamilyMap
An Android application that provides a geographical view into your family history by Dallin Stewart,

Acknowledgements:

The Family Map project was originally created by Jordan Wild. Thanks to Jordan for this significant contribution. I wrote this project as an assignment in my CS 240 Advanced Programming Concepts class at Brigham Young University from February to April of 2022. The overview here comes from the assignment instructions. 

Overview:

Family Map is an application that provides a geographical view of your family history. One of the most exciting aspects of researching family history is discovering your origins. Family Map provides a detailed view of where you came from. It does so by displaying information about important events in your ancestors’ lives (birth, marriage, death, etc.), and plotting their locations on a Google map.

Family Map uses a client/server architecture, which means it consists of two separate programs (a "client" program and a "server" program). The Family Map client is an Android app that lets a user view and interact with their family history information. The Family Map server is a regular, non-Android Java program that runs at a publicly-accessible location in the "cloud" or locally on your machine. When a user runs the Family Map client app, they are first asked to login. After authenticating the user’s identity with the Family Map server, the client app retrieves the user’s family history data from the server. The server is responsible for maintaining user accounts as well as dispensing family history data for Family Map users.

Warning:

A warning to any students in CS 240 at BYU. I'm sure there are lots of other repositories out there from other students like me, but if you've found my program look at this code, you are cheating. Please be honest in your academics, and do not search for, read, or copy code from past students. The professors can detect copied code, so use some of the many other resources you have available to debug.

Instructions:

Open the Family Map Server first. I wrote this part of the project in IntelliJ. The server must be running in order for the client side to function.
Open the Family Map Client. I wrote this part of the project in Android Studio. Install an emulator. It does not matter which model as long as it is recent enough. 

Objectives:

1. Designing, implementing, and testing a large, multi-faceted program

2. Relational database concepts and programming

3. Creation of server programs that publish web APIs

4. Automated unit and integration testing

5. Object-Oriented Design

6. User Interface Programming

7. Native Android Development

8. Calling external services through Web APIs
