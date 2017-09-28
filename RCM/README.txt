To use the program please download the rcm.jar file
I've added some initial test data that can be used to play around with the program. This data is in Data.ser. Please add this file to the same directory as rcm.jar and restart the program to load in all the information.

IMPORTATN: Press save before exiting the program or else all the changes made since you last pressed save will be erased!

Explination of program:

This program is meant to be used to schedule kids horseback ridding lessons at stables.
The user can create lessons, assign kids to that lesson and then the program will assign a horse to that kid for that lesson.

How To Use:

When initialy starting the program, there will be no horses, students, or lessons in the data base (sample data is provided in Data.ser). Here are some instructions to get you started with using the program.


Horses:

The first thing you'll want to do is create some horses. This can be done by clicking the 'Add Horse' button in the MainMenu. 
A horses attributes:
	- Name: the name of the horse
	- Max Hours: the maximum number of hours a horse can be ridden on a day
	- Min Hours: the minimum number of hours a horse can be ridden on a day
	- Break Time: the amount of hours a horse needs a break after having been ridden in a lesson
	- Can Jump: whether this horse can ride in Jump lessons

Simply fill in all the fields (they have been filled with a default value) and press 'Add' to add the horse. Press 'Cancel' to go back to the MainMenu.


Students:

After having create some horses it is now time to create some students (kids). This can be done by clicking the 'Add Student' button in the MainMenu.
Student attributes:
	- Name: the name of the student
	- Student Prefs: the horses that a student (kid) prefers to ride. Order here matters as the first horse entered is more likely to be picked for a lesson.
	- Teacher Prefs: the horses that the teacher would prefer the kid to ride. Order matters here as well.
	- Horse not Pref: A horse that the kid does not want to ride. This horse will only be chosen for the kid if no other option is available.

Fill in all the desired fields and press 'Add' to add the person. Press 'Cancel' to go back to the MainMenu.
Only the Name field is required to be filled in. For ease of use, you can enter in the horses name in the dropdown box, or you can simply use the dropdown option to search for the horse.


Lesson:

Now that we have some horses and students to add to a lesson we can create a lesson. This can be done by clicking the 'Add Lesson' button in the MainMenu.
Lesson attributes:
	- Students: All the students that will participate in the lesson (A max of 10 for now).
	- Horses: The horse that this student will be assigned for this lesson. 
	- Date: The date on which this lesson will take place
	- Type: What type of lesson this is, this will effect which horses will be able to ride in this lesson.
	- Start Time: The starting time of the lesson, put in hh:mm format with a 24 hour clock (eg. 5:45 pm would be 17:45)
	- End Time: the ending time of the lesson, put in hh:mm format with a 24 hour clock (eg. 6:45 pm would be 18:45)
	- Lesson Number: A number assigned to the lesson. The purpose for this number is to distinguish lessons that happen at the exact same time. The actual value of this number has no meaning.

Fill in all the desired fields and press 'Add' to add the lesson. Press 'Cancel' to go back to the MainMenu.
At least 1 student needs to be in a lesson. 
The lesson number needs to be unique only in reference to other lessons that have the same start and end time.
	eg. Good Examples:
			First Lesson: starttime 12:30 end time 13:30 lessonnum 1
			Second Lesson: starttime 12:30 end time 13:30 lessonnum 2

			First Lesson: starttime 12:30 end time 13:30 lessonnum 1
			Second Lesson: starttime 13:30 end time 14:30 lessonnum 1

	    Bad Example:
			First Lesson: starttime 12:30 end time 13:30 lessonnum 1
			Second Lesson: starttime 12:30 end time 13:30 lessonnum 1



To see all the lessons, click on the 'Calendar' button in the MainMenu. Then click on the specific date and to the right it will show all the lessons on that date.