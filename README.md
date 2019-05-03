
# GenieLogiciel - Flight Sharing System - Groupe 2

Groupe members:

EL ARABI Younes
BERRICHE Hamza  
BOUHASSOUNE Yassine  
REBAA Meriam  
SABOUR Nabil  
TAISNE Benjamin  
WEI Fengyi  

This project is to create a flight sharing system which offers a platform where pilots can publish their flights and select passengers and where passengers can search and book flights.


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 

### Prerequisites

You need to installer these following softwares in your computer to run this project

1. JAVA 1.8
2. Maven 
3. Elastic Search 6.1+
4. Kibana 6.1+ (to test or debug if you have any searching problems)

Attention: Kibana should be the same version as your Elastic Search


### Installing and running this project

A step by step series of examples that tell you how to get a development env running

1. Download the whole project in your computer.
2. Run ElasticSearch in your computer.
3. Find the "pom.xml" file, right click this file, select "Run As" and choose "Maven generate-sources". This will help you generate all the necessairy tools that we need to use.
4. Run "src/main/java/dao/Data.java". This will help you to initialize your data base and fill some data exemples in your data base.
5. Run "src/main/java/dao/jetty_jersey/JettyMain.java" to start your server.
6. Go to "http://localhost:8080/" and you can start to try this project!


## If you have errors running this project

1. Please check your JAVA version, it should be java 1.8
2. Please check your ElasticSearch version, it should be 6.1+
3. Please check if you have run Data.java before running JettyMain.java
4. Please check your adress is not already in used by entering 

		lsof -i:8080 

	in your terminal. If so, kill this programme.


## Something you may pay attention when using our project


### Please DON'T opening two tabs at the same time
If you want to tester as a passenger and tester as a pilot at the same time, please don't open two tabs at the same brower, because we are using cookies to stock user information and sometimes two tabs may cause errors. Here are some tips for you:

1. Open on tab at a brower, and open another tab at some brower else.
2. Change your account by disconnecting and login at the same tab to test their functions.


### I click the button and nothing happens?
Please be patient and wait for a moment, we are sending you emails and this takes some time :) 

If there's any errors we will sent you an alert massenger, so if nothing happens, just wait for a while.

### When searching a flight

1. All the flights set by default are in Feb 2019 from Paris, Lyon, Nic and Marseille. Before you tape any texts to search a flight, please check Data.java for more information. 
2. Our searching polity is : 
 
   First we search flights that satisfy both conditions (departure location and departure date) and we show the available flights to you.
   
   If we find nothing, we search flights that only match departure date and we will show them to you.
   
   If we still find nothing, we search flights that only match departure location.
   
   If we still find nothing, we will tell you that there is no flights matching your request.
   
   Attention :  if you search a flight with a departure date yyyy-mm-dd, you will get a list of flights FROM this date. 
	
   Example: if you search a flight at 2019-02-12, you will get a list of flights depart from : 2019-02-12, 2019-02-13,2019-02-15 etc.

3. If you can't find a flight by date but you can find it in Kibanna :
	
   Please try to use Kibanna to change the type of departureDate of the flight_index from 'default' to  'String'.
4. If you get the list successfully, you can sort each colonne by clicking the colonne title and search flights based on the list by entering your information in  the top right search box.

### When signing in your account
1. If you don't want to creat a new account by yourself, you can check the Data.java class, choose a passenger or a pilot to login.

2. Please don't save your password with your browser! Sometimes you may try to edit your infomation including your password, and the browser may not update your new password, you are in risk of losing your account.(Even though you can refind your account with Kibana index_Passenger or index_Pilot).

3. If you find that you can't login with your username and your password:
	
Maybe you have changed your information before and you have forgetten. Try Kibana to find your account.
	
If you don't want to use Kibana: you can stop JetteyMain.java, delete your data base by entering following command in your terminal.  
	
	curl -X DELETE 'http://localhost:9200/_all'  
	
And then you can run Data.java and your database is rebuilt. You can either choose an account in the data base or create one by yourself.

### When editing your account information
1. please DON'T change your username and retape your original username in this box.

### What's the differnce between 'My Flights' and 'My Reservations' ?

When a passenger books a flight with a number of seats that he/she wants, both the pilot and the passenger will see this flight in their account/My Reservations. 

However, this flight will not be showed in the passenger account/My flights because it has not been approved by the pilot yet.
 
For pilots, 'My Flights' contains a list of flights that this pilots have published. 

For passengers, 'My Flights' contains a list of flights that required by this passenger and that have already been approved by the pilot.

For pilots, 'My Reservation' contains a  list of requests sent by passengers with a certain number of seats that they want to book. 

The pilot can click 'agree' to approve this flight, and this flight will not show anymore in the list of 'My Reservation' in pilot's account. What's more, the flight in pilot's account/My Flight will also update its available seats depending on the request. 

If the pilot click 'refuse', this flight will be deleted from 'My Reservation' and nothing happens.

For passengers, 'My Reservation' contains a  list of requests that he/she sent. If one of the flight is approved by the pilot, the passenger will receive an email and this flight will be deleted from  'My Reservation' and will be added in 'My Flights'. 

If this flight is refused by the pilot, the passenger will receive an email, this flight will be deleted from  'My Reservation' and nothing else happens.



