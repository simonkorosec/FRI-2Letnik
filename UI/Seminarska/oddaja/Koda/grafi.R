
library(lubridate)

pod <- read.table("podatkiSem1.txt", sep=",", header=T)
pod$Datum <- as.Date(pod$Datum, "%Y-%m-%d")
pod$Leto <- as.factor(year(pod$Datum))



#
# Distribucija vrednosti atributov
#

hist(pod$PM10, xlab="Vrednost", ylab="Frekvenca", main="Porazdelitev atributa PM10")
hist(pod$O3, xlab="Vrednost", ylab="Frekvenca", main="Porazdelitev atributa O3")

#
# Koncentracija ozona skozi leto
#

v <- c()
meritve <- as.factor(pod$Datum)
pod$Datum <- as.factor(pod$Datum)

for (i in 1:k){
	m <- meritve[i]
	v[i] <- mean(pod$O3[pod$Datum == m])

}
plot(x=meritve, y=v, xlab="Datum meritve", ylab="Vrednost ozona", main="Vrednost koncentracije ozona skozi leta")


plot(x=pod$O3,y=pod$Temperatura_lokacija_min,xlab="Koncentracija ozona", ylab="Temperatura_min", main="Koncentracija odvisna od temperature")
 #linearna odvisnost, slika1

plot(x=pod$Temperatura_lokacija_min,y=pod$Temperatura_Krvavec_mean,xlab="Temperatura_min", ylab="Krvavec temperatura", main="Linearna odvisnost")
#linearna slika 2
 


