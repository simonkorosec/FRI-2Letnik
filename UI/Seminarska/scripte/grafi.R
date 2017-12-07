
library(lubridate)

pod$Datum <- as.Date(pod$Datum, "%Y-%m-%d")
pod$Leto <- as.factor(year(pod$Datum))


#
# Povp. konc. PM10
#

avgPM10Lj <- c()
avgPM10Kp <- c()


for (i in 1:4){
	leto <- 2012 + i
	avgPM10Lj[i] <- mean(pod$PM10[pod$Postaja == "Ljubljana" & pod$Leto == leto])
	avgPM10Kp[i] <- mean(pod$PM10[pod$Postaja == "Koper" & pod$Leto == leto])
}

Leto <- c(2013,2014,2015,2016)

plot(y=avgPM10Lj, x=Leto, xlab="Leto", ylab="Koncentracija delcev PM10", main="Povpreèna koncetracija delcev PM10 na leto", type="b")
par(new=T)
plot(avgPM10Kp, axes=F, ylab="", xlab="", col="red" , type="b")




#
# Povp. konc. O3
#

avgO3Lj <- c()
avgO3Kp <- c()


for (i in 1:4){
	leto <- 2012 + i
	avgO3Lj[i] <- mean(pod$O3[pod$Postaja == "Ljubljana" & pod$Leto == leto])
	avgO3Kp[i] <- mean(pod$O3[pod$Postaja == "Koper" & pod$Leto == leto])
}

Leto <- c(2013,2014,2015,2016)

plot(y=avgO3Lj, x=Leto, xlab="Leto", ylab="Koncentracija ozona", main="Povpreèna koncetracija ozona na leto", type="b")
par(new=T)
plot(avgO3Kp, axes=F, ylab="", xlab="", col="red" , type="b")