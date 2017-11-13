sem <- read.table(file="podatkiSem1.txt", sep=",", header=TRUE)
sem[,27]<-cut(sem[,27],c(-Inf,60,120,180,Inf),labels=c("NIZKA","SREDJA","VISOKA","EKSTREMNA"))
barplot(table(sem$O3), xlab="Ocena", ylab="dnevna koncentracija", main="Koncentracija za ozon") #koncentracija za ozon Ocena
-------------------------------------------------------


 sem[,26]<-cut(sem[,26],c(-Inf,35,Inf),labels=c("NIZKA","VISOKA"))

barplot(table(sem$PM10), xlab="Ocena", ylab="dnevna koncentracija PM10", main="Dnevne koncentracije")
———————————————————————————
d<-density(sem$O3)
plot(d,main="Density koncentracije")

t<-density(sem$PM10)
plot(t,main="Density PM10")
———————————————————————————

