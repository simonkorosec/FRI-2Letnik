pod 			<- read.table("podatkiSem1.txt", sep=",", header=T)

# Nerabiš same 0
pod$Glob_sevanje_min <- NULL

# Sprememba tipa v datum
pod$Datum <- as.Date(pod$Datum, "%Y-%m-%d")
pod$Mesec <- months(pod$Datum)
pod$Mesec <- as.factor(pod$Mesec)
pod$Datum <- NULL


# Delitev na letne case
zima <- pod$Mesec == "februar" | pod$Mesec == "januar" | pod$Mesec == "december"
pomlad <- pod$Mesec == "marec" | pod$Mesec == "april" | pod$Mesec == "maj"
poletje <- pod$Mesec == "junij" | pod$Mesec == "julij" | pod$Mesec == "avgust"
jesen <- pod$Mesec == "september" | pod$Mesec == "oktober" | pod$Mesec == "november"

pod$Letni_cas[zima] <- "zima"
pod$Letni_cas[pomlad] <- "pomlad"
pod$Letni_cas[poletje] <- "poletje"
pod$Letni_cas[jesen] <- "jesen"

pod$Letni_cas <- as.factor(pod$Letni_cas)




mae <- function(observed, predicted)
{
	mean(abs(observed - predicted))
}

rmae <- function(observed, predicted, mean.val) 
{  
	sum(abs(observed - predicted)) / sum(abs(observed - mean.val))
}

mse <- function(observed, predicted)
{
	mean((observed - predicted)^2)
}

rmse <- function(observed, predicted, mean.val) 
{  
	sum((observed - predicted)^2)/sum((observed - mean.val)^2)
}



sel <- sample(1:nrow(pod), size=as.integer(nrow(pod)*0.7), replace=F)
learn <- pod[sel,]
test <- pod[-sel,]

lm.model<- lm(O3~. , learn)
predicted <- predict(lm.model, test)
mae(test$O3, predicted)
rmae(test$O3, predicted, mean(learn$O3))




