Instructions to run from docker:

1 - Download Dockerfile

2 - Run # docker build -t roverdocker .

3 - Run # docker run -p 8080:8080 roverdocker

4 - Go to localhost:8080



Display images taken from the mars rover

This app reads a text with dates and gets a random image from the mars rover from the dates given

Programming Exercise The exercise we’d like to see is to use the NASA API described here to build a project in GitHub that calls the Mars Rover API and selects a picture on a given day. We want your application to download and store each image locally.

Here is an <https://github.com/jlowery457/nasa-exercise | example> of this exercise done by one of our senior developers. This is the level of effort we are looking for from someone who wants to join the LAO development team.

Acceptance Criteria

Please send a link to the GitHub repo via matt.hawkes@livingasone.com when you are complete.

Use list of dates below to pull the images were captured on that day by reading in a text ﬁle: 02/27/17 June 2, 2018 Jul-13-2016 April 31, 2018

Language needs to be Java.

We should be able to run and build (if applicable) locally after you submit it

Include relevant documentation (.MD, etc.) in the repo

Bonus

Bonus - Unit Tests, Static Analysis, Performance tests or any other things you feel are important for Deﬁnition of Done
Double Bonus - Have the app display the image in a web browser
Triple Bonus – Have it run in a Docker or K8s (Preferable)
