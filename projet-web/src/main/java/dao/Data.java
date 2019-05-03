package dao;

import java.util.List;
import java.util.LinkedList;

import elements.Airplane;
import elements.Airport;
import elements.Flight;
import elements.Passenger;
import elements.Pilot;
import elements.Reservation;



/**
 * This class fills data in the data base
 * @author fengyiwei
 *
 */
public class Data {

	public static void main(String[] args) throws Exception {
		AirportDaoImpl airport=new AirportDaoImpl();
		AirplaneDaoImpl airplane= new AirplaneDaoImpl();
		PassengerDaoImpl passenger=new PassengerDaoImpl();
		PilotDaoImpl pilot = new PilotDaoImpl();
		FlightDaoImpl flight= new FlightDaoImpl();
		ReservationDaoImpl reservation= new ReservationDaoImpl();
		
		Airplane plane1=new Airplane(1,"Boeing747",232);
		Airplane plane2=new Airplane(2,"Boeing747",232);
		Airplane plane3=new Airplane(3,"Boeing749",237);
		Airplane plane4=new Airplane(4,"Boeing754",266);
		Airplane plane5=new Airplane(5,"Boeing712",343);
		Airplane plane6=new Airplane(6,"Boeing755",198);
		
		
		Airport port1=new Airport("Paris Charles de Gaulle","5632X","Paris");
		Airport port2=new Airport("Paris Orly Airport","6332X","Paris");
		Airport port3=new Airport("Nice Airport","47232X","Nice");
		Airport port4=new Airport("Lyon-Saint Exupéry Airport","58892X","Lyon");
		Airport port5=new Airport("Marseille Airport","0932X","Marseille");
		
		
		Passenger p1=new Passenger("1","user1","sfqmfj","Bob","1999-11-12","skljfqmsndiq@gmail.com");
		Passenger p2=new Passenger("2","user2","fmlHFsfq","Alex","1994-01-02","sdnfqi@gmail.com");
		Passenger p3=new Passenger("3","user3","sjdfqmnsf","Amy","1998-01-11","sqnfi@gmail.com");
		Passenger p4=new Passenger("4","user4","defqsfqfd","Paul","1899-03-23","sqfdqf@gmail.com");
		Passenger p5=new Passenger("5","user5","irenczee","Harry","1993-09-12","zefqd@gmail.com");
		Passenger p6=new Passenger("6","user6","qdokmfqoefoi","Stark","1999-12-22","dqfdqf@gmail.com");
		Passenger p7=new Passenger("7","user7","qofimqf","Alice","1995-09-23","dfqfdqf@gmail.com");
		
		List<Passenger> list1= new LinkedList<Passenger>();
		list1.add(p1);
		list1.add(p2);
		List<Passenger> list2= new LinkedList<Passenger>();
		list2.add(p3);
		list2.add(p4);
		List<Passenger> list3= new LinkedList<Passenger>();
		list3.add(p5);
		List<Passenger> list4= new LinkedList<Passenger>();
		list4.add(p6);
		List<Passenger> list5= new LinkedList<Passenger>();
		list5.add(p7);
		list5.add(p1);
		list5.add(p4);
		list5.add(p5);
		List<Passenger> list6= new LinkedList<Passenger>();
		list6.add(p1);
		list6.add(p2);
		list6.add(p3);
		list6.add(p4);
		list6.add(p7);
		list6.add(p5);
		
		
		Pilot t1= new Pilot("1","pilot1","sfqmfj","Piere","1999-01-22","one-year-pilot","good","sklqfefvsndiq@gmail.com"); 
		Pilot t2= new Pilot("2","pilot2","ssdfqj","Ben","1999-09-23","two-year-pilot","good","qmsndiq@gmail.com"); 
		Pilot t3= new Pilot("3","pilot3","fqgeqmfj","Penny","1999-05-19","fifteen-year-pilot","great","aaaaadiq@gmail.com"); 
		Pilot t4= new Pilot("4","pilot4","egtsqmfj","Emily","1999-08-11","one-day-pilot","no qualifications","skbbbbbbiq@gmail.com");
		
		Flight f1=new Flight("1","2019-02-03","10:00",plane1,port1,port2,t1,list1,112,376,"1h");
		Flight f2=new Flight("2","2019-02-03","11:00",plane2,port2,port3,t2,list2,132,396,"2h");
		Flight f3=new Flight("3","2019-02-03","12:30",plane3,port3,port4,t3,list3,12,374,"3h");
		Flight f4=new Flight("4","2019-02-13","09:00",plane1,port4,port2,t4,list4,21,373,"1h");
		Flight f5=new Flight("5","2019-02-13","10:00",plane2,port5,port1,t1,list5,12,346,"1h");
		Flight f6=new Flight("6","2019-02-13","12:10",plane6,port1,port5,t2,list6,154,376,"1h");
		Flight f7=new Flight("7","2019-02-13","12:40",plane1,port2,port4,t3,list1,1533,176,"1h");
		Flight f8=new Flight("8","2019-02-13","12:50",plane2,port3,port3,t4,list2,112,456,"1h");
		Flight f9=new Flight("9","2019-02-15","08:00",plane3,port4,port2,t1,list3,112,326,"1h30m");
		Flight f10=new Flight("10","2019-02-15","21:00",plane4,port5,port1,t2,list4,112,276,"1h");
		Flight f11=new Flight("11","2019-02-16","16:00",plane5,port1,port2,t3,list5,100,476,"1h");
		Flight f12=new Flight("12","2019-02-17","18:10",plane6,port2,port4,t4,list6,211,476,"1h");
		Flight f13=new Flight("13","2019-02-18","12:00",plane1,port3,port2,t1,list1,45,376,"1h23m");
		Flight f14=new Flight("14","2019-02-20","06:00",plane2,port4,port3,t2,list2,32,376,"1h");
		Flight f15=new Flight("15","2019-02-22","03:00",plane3,port5,port5,t3,list3,65,376,"1h");
		Flight f16=new Flight("16","2019-02-22","04:20",plane4,port1,port1,t4,list4,54,376,"1h");
		Flight f17=new Flight("17","2019-02-22","04:45",plane5,port2,port4,t1,list5,25,676,"1h33m");
		Flight f18=new Flight("18","2019-02-22","08:00",plane6,port3,port4,t1,list6,12,176,"1h");
		Flight f19=new Flight("19","2019-02-22","12:10",plane1,port4,port2,t2,list1,54,376,"1h");
		Flight f20=new Flight("20","2019-02-22","15:50",plane2,port5,port2,t3,list2,12,376,"1h");
		Flight f21=new Flight("21","2019-02-26","12:30",plane3,port1,port5,t4,list3,65,376,"1h");
		Flight f22=new Flight("22","2019-02-26","12:00",plane4,port2,port5,t1,list4,3,116,"1h");
		Flight f23=new Flight("23","2019-02-27","23:00",plane5,port3,port2,t2,list5,32,376,"1h");
		Flight f24=new Flight("24","2019-02-28","11:00",plane6,port4,port1,t3,list6,16,176,"1h");
		
		Reservation r1=new Reservation("1",f1,p1,2);
		Reservation r2=new Reservation("2",f1,p2,1);
		Reservation r3=new Reservation("3",f3,p1,7);
		Reservation r4=new Reservation("4",f4,p1,2);
		Reservation r5=new Reservation("5",f6,p4,1);
		Reservation r6=new Reservation("6",f8,p5,1);
		Reservation r7=new Reservation("7",f21,p3,2);
		
		
		airport.addElement(port1);
		airport.addElement(port2);
		airport.addElement(port3);
		airport.addElement(port4);
		airport.addElement(port5);
		
		airplane.addElement(plane1);
		airplane.addElement(plane2);
		airplane.addElement(plane3);
		airplane.addElement(plane4);
		airplane.addElement(plane5);
		airplane.addElement(plane6);
		
		passenger.addElement(p1);
		passenger.addElement(p2);
		passenger.addElement(p3);
		passenger.addElement(p4);
		passenger.addElement(p5);
		passenger.addElement(p6);
		passenger.addElement(p7);
		
		pilot.addElement(t1);
		pilot.addElement(t2);
		pilot.addElement(t3);
		pilot.addElement(t4);
		
		flight.addElement(f1);
		flight.addElement(f2);
		flight.addElement(f3);
		flight.addElement(f4);
		flight.addElement(f5);
		flight.addElement(f6);
		flight.addElement(f7);
		flight.addElement(f8);
		flight.addElement(f9);
		flight.addElement(f10);
		flight.addElement(f11);
		flight.addElement(f12);
		flight.addElement(f13);
		flight.addElement(f14);
		flight.addElement(f15);
		flight.addElement(f16);
		flight.addElement(f17);
		flight.addElement(f18);
		flight.addElement(f19);
		flight.addElement(f20);
		flight.addElement(f21);
		flight.addElement(f22);
		flight.addElement(f23);
		flight.addElement(f24);
		
		System.out.println("Reservation :");
		reservation.addElement(r1);
		reservation.addElement(r2);
		reservation.addElement(r3);
		reservation.addElement(r4);
		reservation.addElement(r5);
		reservation.addElement(r6);
		reservation.addElement(r7);
		
		

	}

}
