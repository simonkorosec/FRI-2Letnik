#
# Priprava nove učne množice
#



pod <- read.table("podatkiSem1.txt", sep=",", header=T)
pod$Glob_sevanje_min <- NULL
pod$Datum <- as.Date(pod$Datum, "%Y-%m-%d")
pod$Mesec <- months(pod$Datum)
pod$Mesec <- as.factor(pod$Mesec)
zima <- pod$Mesec == "februar" | pod$Mesec == "januar" | pod$Mesec == "december"
pomlad <- pod$Mesec == "marec" | pod$Mesec == "april" | pod$Mesec == "maj"
poletje <- pod$Mesec == "junij" | pod$Mesec == "julij" | pod$Mesec == "avgust"
jesen <- pod$Mesec == "september" | pod$Mesec == "oktober" | pod$Mesec == "november"
pod$Letni_cas[zima] <- "zima"
pod$Letni_cas[pomlad] <- "pomlad"
pod$Letni_cas[poletje] <- "poletje"
pod$Letni_cas[jesen] <- "jesen"
pod$Letni_cas <- as.factor(pod$Letni_cas)
pod$Mesec <- NULL
pod$Datum <- NULL

# Zapis nove množice podatkov
write.table(pod, "noviPodatki.txt", sep=",", row.names=FALSE)


