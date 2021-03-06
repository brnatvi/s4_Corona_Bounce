[main]: https://gaufre.informatique.univ-paris-diderot.fr/raveneau/corona-bounce
[coverage-shield]: https://img.shields.io/badge/Coverage-35%25-yellow
[comment-shield]: https://img.shields.io/badge/Javadoc-100%25-brightgreen
[ ![coverage-shield][] ][main]
[ ![comment-shield][] ][main]


# **Overview**

Corona Bounce is an epidemic simulation tool with a GUI interface.

It provides possibility to compare two populations during un epidemic.
It is also possible to variate population’s and disease’s parameters and apply four government strategies.

# **How to use**

## Compile (to do in code/)
```
mvn compile
```

## Run
```
java -jar code/cb1.0.9.jar
```

## Re-Run with Maven (to do in code/)
```
./run.sh
```

## Open JavaDoc
Launch code/docs/javadoc/apidocs/index.html on the web browser.

# **Maintainers:**

* Zahra Alliche
* Natalia Bragina
* Anna Golikova
* Kenza Sahi
* Emilien Raveneau Grisard


# **Features**

It's possible to modify follows parameters:
* Size of population
* Duration of incubation
* Duration of healing
* Duration of immunity
* Radius of contamination
* Number of boundaries
* Boundaries closing speed 



![](https://github.com/brnatvi/Corona_Bounce/blob/master/project_1_resized.gif)

Following scenarios are available to imitate government strategies:

* «Soft Lockdown» - all people can move but in limited area
* «Boundaries» - closure of borders for travel 
* «Strict Lockdown» - only police, emergency, medicals, delivery etc can move
* «Soft Lockdown + Boundaries»
* «No scenario»


![](https://github.com/brnatvi/Corona_Bounce/blob/master/Project_2_resize(1).gif)


![](https://github.com/brnatvi/Corona_Bounce/blob/master/project_3_resized.gif)


Help in shape of tooltips is available. 
To use it please press "?" button, then move the mouse cursor to a UI element, hold mouse cursor above the element for a while (1-2 seconds) and help tooltip will popup.  
