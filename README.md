# Innovaccer_Project
-> This is a Customer Check in Android Application.
->First Screen would contain a login for either a Host or a Customer.
->If a Host is already created then the application will check from backend and will disable the Button which will create a Host.
  Otherwise Host button would be enable and on clicking,a new form would appear which has to be filled by the host.
->On Clicking Customer button,form for the Customer would pop up which has to be filled and need to be submitted.As the Customer form would be 
  submitted,an email and sms containing the Customer information would be send to the host respective email and contact number.
->When the customer would successfully Check in , it would move the customer to the home screen where 1 Button would be Present
  named "Check Out".
->On Clicking "Check Out" button ,information of the customer with the host's name and email would be sent as an email to the respective 
  Customer who checked out.
  
Point To remember:
(1)-> Because of lack of time,"Check in Field" and "check out field" are just string fields with maximum length of 4 i.e Customer has to enter 
      "Check in time" and "Check out time" in 24 hour format.
(2)-> Every time a new Customer checks in a user is signed in the backend , so agter check out , a new customer with same  email id could 
      not be enterd because that customer has already signed in before.(I could not correct that because of lack of time,but it will
      not take much time to solve.)
(3)-> Backend is processed with Firebase and Android Application is created in Android Studio.
(4)-> Meaningful UI is also provided for better experience using Material Design.

"Thanks For Giving Time to Read Not such an Interesting README".
