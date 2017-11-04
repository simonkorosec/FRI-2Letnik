
#
#
# UVOD V KLASIFIKACIJO
#
#

#
# Nacrt dela:
#
# - Nalozili bomo podatke o NBA igralcih iz vhodne datoteke
# - Razdelili bomo podatke na ucno in testno mnozico
# - Zgradili bomo odlocitveno drevo s pomocjo ucne mnozice
# - Kvaliteto zgrajenega drevesa bomo ocenili s pomocjo testne mnozice
#



# Prenesite datoteko "players.txt" v lokalno mapo. To mapo nastavite kot 
# delovno mapo okolja R. To lahko naredite s pomocjo ukaza "setwd" oziroma iz 
# menuja s klikom na File -> Change dir...
# 
# Na primer:
# setwd("c:\\vaje\\data\\")


# Nalozimo ucno mnozico o igralcih
players <- read.table("players.txt", header = T, sep = ",")


# Povzetek o atributih v podatkovni mnozici
summary(players)


############################################################################
#
# POZOR!!!!!!!!!
#
# Atribut Id nima uporabne vrednosti pri generalizaciji znanja, zato ga bomo 
# odstranili iz ucne mnozice
#
############################################################################


players$id <- NULL
names(players)


############################################################################
#
#
# PREPRICAJTE SE, da je atribut "id" odstranjen !!!!!!!!!!
#
#
############################################################################







############################################################################
#
# Primer 1
#
# Napovedovanje igralnega polozaja
#
############################################################################

#
# Zelimo zgraditi model, ki bo napovedal igralno pozicijo glede na podane 
# lastnosti igralca.
#
# Ciljni atribut "position" je diskreten - zato govorimo o klasifikaciji.   
#
# Zelimo preveriti, ali model zgrajen na podlagi zgodovinskih podatkov lahko 
# uporabimo za napovedovanje igralnega polozaja novih igralcev v prihodnje. 
#

# Podatke o igralcih bomo razdelili na ucno in testno mnozico.
# V ucni mnozici bodo igralci, ki so igrali vkljucno do leta 1999.
# V testni mnozici bodo igralci, ki so zaceli kariero po letu 1999.

learn <- players[players$lastseason <= 1999,]
test <- players[players$firstseason > 1999,]


# Podatke smo razdelili glede na vrednosti "firstseason" in "lastseason".
# Iz tega razloga sta omenjena atributa neuporabna pri klasifikaciji, zato
# ju odstranimo.

learn$firstseason <- NULL
learn$lastseason <- NULL

test$firstseason <- NULL
test$lastseason <- NULL


# Poglejmo osnovne karakteristike ucne in testne mnozice 
# (stevilo primerov, porazdelitev ciljne spremenljivke).

nrow(learn)
table(learn$position)

nrow(test)
table(test$position)


#
#
# VECINSKI KLASIFIKATOR
#
#

# Vecinski razred je razred z najvec ucnimi primeri
which.max(table(learn$position))

majority.class <- names(which.max(table(learn$position)))
majority.class


# Izracunajmo tocnost vecinskega klasifikatorja
# (delez pravilnih napovedi, ce bi vse testne primere klasificirali v vecinski razred)

sum(test$position == majority.class) / length(test$position)



#
#
# ODLOCITVENA DREVESA
#
#

# Za gradnjo odlocitvenih dreves potrebujemo funkcijo iz knjiznice "rpart".
# Nalozimo jo
library(rpart)

# Zgradimo odlocitveno drevo
dt <- rpart(position ~ ., data = learn)

#
# Prvi argument funkcije rpart je formula, ki doloca kaksen model zelimo zgraditi.
# Drugi argument predstavlja ucne primere, na podlagi katerih se bo zgradil model.
#
# Formula doloca ciljni atribut (levo od znaka "~") in atribute, ki jih model lahko 
# uporabi pri napovedovanju vrednosti ciljnega atributa (desno od znaka "~").
#
# Formula "position ~ . " oznacuje, da zelimo zgraditi model za napovedovanje 
# ciljnega atributa "position" s pomocjo vseh ostalih atributov v ucni mnozici.
#
# Ce bi, na primer, zeleli pri gradnji modela za "position" uporabiti samo 
# atribut "height", bi to oznacili s formulo "position ~ height". Ce bi pa zeleli 
# uporabiti atribute "height", "weight" in "pts", bi to oznacili 
# s formulo "position ~ height + weight + pts".
#


# Izpisimo nase drevo v tekstovni obliki
dt

# Izrisimo drevo (izris v dveh ukazih)
plot(dt)
text(dt, pretty = 0)

# Neznani primer klasificiramo tako, da zacnemo pri korenu drevesa in potujemo po
# ustreznih vejah do lista. V vsakem notranjem vozliscu testiramo pogoj in, ce je
# le-ta izpolnjen, nadaljujemo v levem sinu, sicer se premaknemo v desnega. 
# Listi drevesa dolocajo razred, v katerega klasificiramo neznani primer.

# Prave vrednosti testnih primerov
observed <- test$position
observed

# Napovedane vrednosti modela
# Uporabimo funkcijo "predict", ki potrebuje model, testne primere in obliko, 
# v kateri naj poda svoje napovedi. Nastavitev "class" pomeni, da nas zanimajo
# samo razredi, v katere je model klasificiral testne primere.
 
predicted <- predict(dt, test, type = "class")
predicted

# Zgradimo tabelo napacnih klasifikacij
t <- table(observed, predicted)
t

# Elementi na diagonali predstavljajo pravilno klasificirane testne primere...

# Klasifikacijska tocnost modela
sum(diag(t)) / sum(t)


# Lahko napisemo funkcijo za izracun klasifikacijske tocnosti
CA <- function(prave, napovedane)
{
	t <- table(prave, napovedane)

	sum(diag(t)) / sum(t)
}

# Klic funkcije za klasifikacijsko tocnost...
CA(observed, predicted)






# Druga oblika napovedi modela (nastavitev "prob") vraca verjetnosti, 
# da posamezni testni primer pripada dolocenemu razredu.

# Napovedane verjetnosti pripadnosti razredom (odgovor dobimo v obliki matrike)
predMat <- predict(dt, test, type = "prob")
predMat

# Prave verjetnosti pripadnosti razredom 
# (dejanski razred ima verjetnost 1.0 ostali pa 0.0)
obsMat <- model.matrix( ~ position-1, test)
obsMat

# Funkcija za izracun Brierjeve mere
brier.score <- function(observedMatrix, predictedMatrix)
{
	sum((observedMatrix - predictedMatrix) ^ 2) / nrow(predictedMatrix)
}

# Izracunajmo Brierjevo mero za napovedi nasega drevesa
brier.score(obsMat, predMat)




############################################################################
#
# Primer 2
#
# Ali je igralec ekspert za izvajanje prostih metov?
# (ima vsaj 80% uspesnost prostih metov)
# 
############################################################################

#
# Recimo, da nas zanima, ali je igralec ekspert za izvajanje prostih metov.
# Torej, imamo binarni problem (diskretni razred z vrednostima YES in NO)
#


#
# Zgradili bomo novo ucno mnozico na podlagi podatkov iz mnozice "players.txt".
#

# Izberimo igralce, ki so v karieri izvedli vsaj en prosti met
bin.players <- players[players$fta > 0,]

# Uspesnost prostih metov
rate <- bin.players$ftm / bin.players$fta

# Kreirajmo ciljno spremenljivko "ftexpert"
ftexpert <- vector()
ftexpert[rate >= 0.8] <- "YES"
ftexpert[rate < 0.8] <- "NO"
bin.players$ftexpert <- as.factor(ftexpert)

# Odstranimo atribute "fta" in "ftm" iz podatkovne mnozice
bin.players$fta <- NULL
bin.players$ftm <- NULL

summary(bin.players)



# Zgradimo ucno in testno mnozico
bin.learn <- bin.players[1:1500,]
bin.test <- bin.players[-(1:1500),]

# Poglejnmo porazdelitev ciljne spremenljivke v ucni in testni mnozici
table(bin.learn$ftexpert)
table(bin.test$ftexpert)


# Zgradimo odlocitveno drevo
dt2 <- rpart(ftexpert ~ ., data = bin.learn)
plot(dt2)
text(dt2, pretty = 0)


bin.observed <- bin.test$ftexpert
bin.predicted <- predict(dt2, bin.test, type="class")


table(bin.observed, bin.predicted)


# Klasifikacijska tocnost modela
CA(bin.observed, bin.predicted)



# Funkcija za izracun senzitivnosti modela
Sensitivity <- function(observed, predicted, pos.class)
{
	t <- table(observed, predicted)

	t[pos.class, pos.class] / sum(t[pos.class,])
}

# Funkcija za izracun specificnosti modela
Specificity <- function(observed, predicted, pos.class)
{
	t <- table(observed, predicted)

	# identify the negative class name
	neg.class <- which(row.names(t) != pos.class)

	t[neg.class, neg.class] / sum(t[neg.class,])
}

# izberemo razred "YES" za pozitivni razred

Sensitivity(bin.observed, bin.predicted, "YES")
Specificity(bin.observed, bin.predicted, "YES")


