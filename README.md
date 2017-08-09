# Menuz GitHub Repository
**Title**: (Master Thesis) Conception, design and testing of a split menu 
for smartphone  
**Author**: Nathan Magrofuoco  
**Supervisor**: Jean Vanderdonckt  
**Location**: Université Catholique de Louvain (BE)  
**Academic Year**: 2016-2017  

## Abstract
In 1994, Sears and Shneiderman proposed the changing concept of split menu. 
They have radically influenced the way we have designed menus until today. 
Unfortunately, their guidelines haven’t evolved in 20 years and the advent 
of smartphones have led HCI researchers to new and different usability issues. 
Based on their initial study, this master thesis aims to conceive, design and 
test a split menu adapted to smartphone resolutions.  

Along the way, the approaches from various researchers have influenced our 
experiment and diverse menu organizations have also been designed and tested. 
An experimental method has been conducted combining traditional, split, 
responsive, minimised and mixed-initiative menus. The objective of this method 
was to assess the usability of these new menu organizations on smartphones. 
Usability has been studied along with 3 interesting properties: (1) effectiveness, 
(2) efficiency and (3) user satisfaction.  

The experiment proved that split menu may not be the ideal solution for 
smartphones. Another novative menu organization called “responsive” has shown a 
better usability. This dissertation aims to explain the development of the 
experiment and argue the analysis of its results.  

## Repository architecture
* The folder **dissertation/** stores the sources of the written dissertation 
associated to the study.  
* The folder **application/** stores the Android Studio project developed during 
the experiment.   

## Switch the app to English (HOW TO)
1. Go to: application/Menuz/app/src/main/res/values/string_en.xml  
2. Uncomment the content of this file  
3. Go to: application/Menuz/app/src/main/res/values/string.xml  
4. Comment the content of this file  
5. Go to: application/Menuz/app/src/main/java/be/ac/uclouvain/menuz/Utility.java  
6. Comment line 36 (disable the list of French items)  
7. Uncomment line 38 (enable the list of English items)  

