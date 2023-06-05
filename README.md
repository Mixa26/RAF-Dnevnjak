# RAF-Dnevnjak

This was a colloquium project for uni. The app is supposed to be used for organizing chores using a calendar.

When first run, the app will go to a login screen, there is only a default user with the credentials (username, username@gmail.com, A1234).
The login screen has validations for parameters such as the password needs to contain at least one upper case letter and have 5 characters at least...
After the login the calendar will be open to the current month. By scrolling up/down the calendar will dynamically load up previous/next months.
On the top of the screen you will see which month and year you're looking at. On the bottom you will see three buttons from which one is active (Calendar).
The second button is the daily plan which can be only accessed by clicking on a day in the calendar, and the third button is the profile.
In the profile tab you can log out, or change the password (works only while the app is running, when the app is shutdown the memory is wiped and reset
to the old account already mentioned). When changing the password the old and new password must differ and the new password and its confirmatiom must match.

When we click on a day in the calendar a daily plan screen will show up. At the top you can select to see passed obligations (which aren't showed otherwise, 
this is also a reason why you might not be seeing obligations when you add them to past dates). Below it is a search bar and a filter with threee buttons to 
show only obligations with low,mid,high severities. If clicked on again the list will show all obligations. Near the bottom on the right side of the screen you
will see a + button which is used for adding obligations. The obligation contains it's severity (low/mid/high), a title, description and time which must be
reasonable and must not overlap other activities. When created it will show up in the obligations list in the daily plan, you can click on it for the detailed
view. You will also see buttons for editing, deleting the obligation. If you go back to the calendar now you will see that the day you added a obligation to has
been colored based on the highest obligation severity that day (if its high it will be orange, mid is yellow and low is green). That's about it, you can explore
a bit more to see how all of this works :).

The original documentation for this project is in the "RAF Dnevnjak - Specifikacija.docx" file.

# The application itself:

## Splash screen:
![SplashScreen](https://github.com/Mixa26/RAF-Dnevnjak/assets/71144280/e2c3a83e-866b-4484-9f71-c5c83189ff34)
<br>
## Login screen:
![Login](https://github.com/Mixa26/RAF-Dnevnjak/assets/71144280/d83cbee8-3a59-4348-a7cd-d5788184dc84)
<br>
## Calendar:
![Calendar](https://github.com/Mixa26/RAF-Dnevnjak/assets/71144280/f90a095d-dcc5-40ae-9862-a7e6381c5db0)
<br>
## Detailed date:
![Obligations](https://github.com/Mixa26/RAF-Dnevnjak/assets/71144280/5b6062e7-2565-43ac-b812-d61c1404e46f)
<br>
## Add a obligation to a certain date:
![CreateObligation](https://github.com/Mixa26/RAF-Dnevnjak/assets/71144280/a967bc3a-262e-45f8-b2a6-8d86bca0199b)
<br>
## Calendar colours its date based on the most severe obligation that day:
![ColouredCalendar](https://github.com/Mixa26/RAF-Dnevnjak/assets/71144280/3e1b1edb-a711-4e32-b185-2cfe36507168)
<br>
## Filter obligations based on their severity:
![FilterObligations](https://github.com/Mixa26/RAF-Dnevnjak/assets/71144280/8c510eed-1ed0-45df-83c2-ba05dc6b4d5f)
<br>
## Search obligations through the search field:
![SearchObligations](https://github.com/Mixa26/RAF-Dnevnjak/assets/71144280/c5382852-63b4-4128-be0b-bd8f1fc0cad5)
<br>
## Detailed obligation view:
![DetailedObligation](https://github.com/Mixa26/RAF-Dnevnjak/assets/71144280/abdaa1ea-9bd7-4f46-8aa4-4a184d6dbed6)
<br>
## Edit profile screen:
![Profile](https://github.com/Mixa26/RAF-Dnevnjak/assets/71144280/be9b6622-edea-4e63-86a3-3b65f0e7a54b)
<br>
## Change password:
![ChangePassword](https://github.com/Mixa26/RAF-Dnevnjak/assets/71144280/50183867-ead6-4323-bdd0-4b9387bc216f)
<br>
