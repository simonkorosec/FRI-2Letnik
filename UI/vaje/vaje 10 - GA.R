# Uporabljali bomo knjižnico GA
# Prepricajte se, da je knjiznica nalozena (sicer jo nalozite z install.packages)

library(GA)

#
#
# PRIMER 1: Optimizacija enodimenzionalne funkcije
#
#

# Maksimizacija asimetricne funkcije "double claw" je tezavna zaradi velikega stevila lokalnih ekstremov.
# Standardni pristopi, ki se zanasajo na vrednosti odvodov, bi nasli maksimum, najblizji zacetni vrednosti.

f <- function(x) 
{
	y <- (0.46 * (dnorm(x, -1, 2/3) + dnorm(x, 1, 2/3)) +
	(1/300) * (dnorm(x, -0.5, 0.01) + dnorm(x, -1, 0.01) +
	dnorm(x, -1.5, 0.01)) +
	(7/300) * (dnorm(x, 0.5, 0.07) + dnorm(x, 1, 0.07) +
	dnorm(x, 1.5, 0.07)))
	
	y
}

# Izrisemo funkcijo "double claw"
curve(f, from = -3, to = 3, n = 1000)

# Pri optimizaciji te funkcije lahko kot vrednost kriterijske funkcije (fitness function) uporabimo kar vrednost funkcije same.
GA <- ga(type = "real-valued", fitness = f, min = -3, max = 3)

# Vrnjeni objekt lahko izrisemo
plot(GA)
summary(GA)

# Izris resitve
curve(f, from = -3, to = 3, n = 1000)
points(GA@solution, f(GA@solution), col="red")


# Evolucijo populacije in ustreznih vrednosti funkcije za vsako generacijo je mogoce pridobiti
# z definiranjem ustrezne monitor funkcije (funkcija za izris trenutne populacije), ki jo nato 
# podamo v funkcijo ga() kot opcijski argument.

myMonitor <- function(obj) 
{
	curve(f, obj@min, obj@max, n = 1000, main = paste("iteration =", obj@iter))
	points(obj@population, obj@fitness, pch = 20, col = 2)
	rug(obj@population, col = 2)
	Sys.sleep(1)
}

GA <- ga(type = "real-valued", fitness = f, min = -3, max = 3, monitor = myMonitor)


#
#
# PRIMER 2: Prileganje krivulji (curve fitting)
#
#

# Uporabljali bomo podatke o rasti dreves

# Starost drevesa ob meritvi
Age <- c(2.44, 12.44, 22.44, 32.44, 42.44, 52.44, 62.44, 72.44, 82.44, 92.44, 102.44, 112.44)

# Prostornina debla
Vol <- c(2.2, 20.0, 93.0, 262.0, 476.0, 705.0, 967.0, 1203.0, 1409.0, 1659.0, 1898.0, 2106.0)

plot(Age, Vol)

# Ekoloski model za velikost rastline (merjeno s prostornino) kot funkcije starosti je Richardsova krivulja
# f(x) = a*(1-exp(-b*x))^c, kjer so a, b in c parametri modela

# Poskusimo Richardsovo krivuljo prilagoditi nasim podatkom z uporabo genetskih algoritmov

# Najprej definiramo funkcijo f (argument theta je vektor parametrov modela a, b, and c) 
model <- function(params)
{
	params[1] * (1 - exp(-params[2] * Age))^params[3]
}

# Definiramo kriterijsko funkcijo kot vsoto kvadratov razlik med izracunanimi in dejanskimi vrednostmi
myFitness2 <- function(params) 
{
	-sum((Vol - model(params))^2)
}

# Kriterijsko funkcijo je treba maksimizirati s spreminjanjem parametrov v vektorju params, pri podanih podatkih Age in Vol.
# Za izboljsanje iskanja cez prostor parametrov uporabljamo krizanje z uporabo zlivanja (blend crossover): pri 
# podanih starsih x1 in x2 (naj bo x1 < x2), nakljucno izbere resitev v intervalu [x1 - k*(x2-x1), x2 + k*(x2-x1)], 
# kjer k predstavlja konstanto med 0 in 1.

# Pri iskanju omejimo zalogo vrednosti parametra a na interval [1000.0, 5000.0] ter parametrov b in c na interval [0.0, 5.0]

GA2 <- ga(type = "real-valued", fitness = myFitness2, min = c(1000, 0, 0), max = c(5000, 5, 5),
 popSize = 500, crossover = gareal_blxCrossover, maxiter = 5000, run = 200, names = c("a", "b", "c"))


summary(GA2)

# Izrisimo naso resitev

plot(Age, Vol)
lines(Age, model(GA2@solution))



# lahko uporabimo monitor funkcijo za izris trenutno najboljsega modela

myMonitor2 <- function(obj) 
{
	i <- which.max(obj@fitness)
	plot(Age, Vol)
	lines(Age, model(obj@population[i,]), col="red")
	title(paste("iteration =", obj@iter), font.main = 1)
	Sys.sleep(1)
}

GA2 <- ga(type = "real-valued", fitness = myFitness2, min = c(1000, 0, 0), max = c(5000, 5, 5),
 popSize = 500, crossover = gareal_blxCrossover, maxiter = 5000, run = 200, names = c("a", "b", "c"), monitor=myMonitor2)



#
#
# PRIMER 3: Problem 0-1 nahrbtnika
#
#

# Problem 0-1 nahrbtnika je definiran tako: ob podani mnozici predmetov, ki so opisani s tezo in vrednostjo,
# poisci podmnozico teh predmetov, katerih skupna teza ne presega neke podane meje, in je njihova vrednost najvecja.

# vektor vrednosti predmetov
values <- c(5, 8, 3, 4, 6, 5, 4, 3, 2)

# vektor tez predmetov
weights <- c(1, 3, 2, 4, 2, 1, 3, 4, 5)

# najvecja skupna dovoljena teza
Capacity <- 10

# Za resitev tega problema lahko uporabimo binarni GA. Resitev je binarni niz, katerega dolzina je enaka stevilu 
# predmetov v mnozici. Vrednost i-tega bita oznacuje, ali je i-ti predmet izbran (vrednost 1) ali ne (vrednost 0).
# Kriterijska funkcija mora kaznovati resitve, katerih teza presega najvisjo dovoljeno.

knapsack <- function(x) 
{
	f <- sum(x * values)
	penalty <- sum(weights) * abs(sum(x * weights)-Capacity)
	f - penalty
}

GA3 <- ga(type = "binary", fitness = knapsack, nBits = length(weights), maxiter = 1000, run = 200, popSize = 20)

summary(GA3)


#
# Primer 4: URNIK
#

# Manjsi nogometni klub ima mladinsko in clansko mostvo. Trening igralcev zahteva sedem sklopov: 
# kondicijski trening, trening za moc, tehnika, taktika, psiholoska priprava, uigravanje in regeneracija. 
# Ker je klub brez denarja, ima za vsakega izmed zgornjih sklopov zadolzeno samo eno strokovno usposobljeno
# osebo, razen za taktiko in kondicijski trening, kjer zaradi specificnosti treninga, potrebujejo razliène 
# trenerje za clansko in mladinsko ekipo.
# 
# Tedenski obseg treningov je podan v spodnji tabeli:
# 
#+----------+---------------------+-----------------+-----------------+
#| Trener   | Sklop               | Clani           | Mladinci        |
#+----------+---------------------+-----------------+-----------------+
#| Anze     | Trening za moc      | 1 krat tedensko | 1 krat tedensko |
#| Bojan    | Tehnika             | 3 krat tedensko | 3 krat tedensko |
#| Ciril    | Regeneracija        | 2 krat tedensko | 2 krat tedensko |
#| Dusan    | Kondicijski trening | ne izvaja       | 4 krat tedensko |
#| Erik     | Kondicijski trening | 4 krat tedensko | ne izvaja       |
#| Filip    | Uigravanje          | 3 krat tedensko | 3 krat tedensko |
#| Gasper   | Psiholoska priprava | 1 krat tedensko | 1 krat tedensko |
#| Hugo     | Taktika             | 1 krat tedensko | ne izvaja       |
#| Iztok    | Taktika             | ne izvaja       | 1 krat tedensko |
#+----------+---------------------+-----------------+-----------------+
#
# Treningi se izvajajo od ponedeljka do petka v štirih razliènih terminih: 
#    8:00 - 10:00, 10:15 - 12:15, 14:00 - 16:00, and 16:15 - 18:15.
# 
# Omejitve:
#
# - v posameznem terminu se izvaja samo en sklop treninga za mladince in eden za clane (treningi za 
#   mladince in èlane potekajo loceno, kar pomeni, da posamezen trener naenkrat trenira samo eno mostvo). 
# 
# - posamezen sklop treninga se v istem dnevu ne izvaja veckrat.
# 
# - sklop Taktika je namenjen pripravi strategije za naslednjo tekmo. Ker se tekme igrajo ob koncih tedna, 
#   mora biti trening tega sklopa ob èetrtkih v terminu 16:15-18:15. 
#
# - po odigrani tekmi igralci potrebujejo oddih, zaradi tega se ob ponedeljkih treningi ne izvajajo v terminu 8:00-10:00.
# 
# - kondicijski trener Dusan je ob ponedeljkih dopoldne odsoten, zato se njegov sklop ne more izvajati v ponedeljkovih 
#   terminih od 8:00-10:00 in 10:15-12:15.
#
# - trener Bojan je odsoten ob sredah, zato se na ta dan v nobenem terminu ne izvaja sklop Tehnika.
#
#
# Sestavite urnik treningov obeh mostev z upostevanjem vseh zgornjih omejitev!
#
#

# SPREMENLJIVKE
#
# senior     - zahtevano stevilo treningov posameznih sklopov za clane
# youth      - zahtevano stevilo treningov posameznih sklopov za mladince
# staff      - trenerji
# slots      - mozni termini
#

set.seed(1234)

senior     = c(1, 3, 2, 0, 4, 3, 1, 1, 0)
youth      = c(1, 3, 2, 4, 0, 3, 1, 0, 1)
staff      = c(1, 2, 3, 4, 4, 5, 6, 7, 7)
slots      = 5*4

valueBin <- function(timetable)
{
	# organiziramo podatke v polje
	# dan, termin, trener, ekipa

	t <- array(as.integer(timetable), c(5,4,9,2))
	
	violations <- 0

	# preverimo vse pogoje
	
	# ali se vse komponente trenirajo zadosti krat
	for (i in 1:9)
	{
		violations <- violations + abs(sum(t[,,i,1]) - senior[i])
		violations <- violations + abs(sum(t[,,i,2]) - youth[i])
	}	

	# ali je vsak dan posamezen sklop izveden kvecjemu enkrat
	for (i in 1:9)
	{
		violations <- violations + sum(apply(t[,,i,1], 1, sum) > 1)
		violations <- violations + sum(apply(t[,,i,2], 1, sum) > 1)
	}

	# ali ima vsak trener samo eno sklop v danem terminu
	violations <- violations + sum(t[,,,1] == t[,,,2] & t[,,,1] != 0)

	# v posameznem terminu se izvaja samo en sklop treninga za mladince in eden za clane
	for (i in 1:5)
		for (j in 1:4)
		{
			violations <- violations + max(0, sum(t[i,j,,1]) - 1)
			violations <- violations + max(0, sum(t[i,j,,2]) - 1)
		}


	# trening sklopa Taktika mora biti ob èetrtkih v terminu 16:15-18:15
	violations <- violations + (t[4,3,8,1] != 1)
	violations <- violations + (t[4,3,9,2] != 1)

	# ob ponedeljkih se treningi ne izvajajo v terminu 8:00-10:00
	violations <- violations + sum(t[1,1,,])

	# kondicijski trener Dusan je ob ponedeljkih dopoldne odsoten
	violations <- violations + sum(t[1,1:2,4,] == 1)
	
	# trener Bojan je odsoten ob sredah
	violations <- violations + sum(t[3,,2,] == 1)

	
	-violations	
}

myInitPopulation <- function(object)
{
	p <- gabin_Population(object)

	for (i in 1:nrow(p))
	{
		t <- array(p[i,], c(5,4,9,2))
		
		# trening sklopa Taktika mora biti ob èetrtkih v terminu 16:15-18:15
		t[4,3,8,1]=1
 		t[4,3,9,2]=1
		
		# ob ponedeljkih se treningi ne izvajajo v terminu 8:00-10:00
		t[1,1,,] = 0

		# kondicijski trener Dusan je ob ponedeljkih dopoldne odsoten
		t[1,1:2,4,] = 0
		
		# trener Bojan je odsoten ob sredah
		t[3,,2,] = 0

		
		p[i,] <- as.vector(t)
	}

	p
}

GA4 <- ga(type = "binary", fitness = valueBin, nBits = 4*5*9*2,
 popSize = 500, maxiter = 1000, run = 200, population = myInitPopulation, maxFitness=0)


timetable <- function(solution)
{
	t <- array(solution, c(5,4,9,2))

	result <- array(0, c(5, 4, 2))

	for (i in 1:5)
		for (j in 1:4)
		{
			n <- which(t[i,j,,1] == 1)
			if (length(n) > 0)
				result[i,j,1] = n

			n <- which(t[i,j,,2] == 1)
			if (length(n) > 0)
				result[i,j,2] = n
		}

	result
}

t <- timetable(GA4@solution[1,])
t



#
#
# PRIMER 5: Problem trgovskega potnika
#
#

# Podan je seznam mest in razdalj med njimi (za vsak par mesto - mesto). Kaksna je najkrajsa pot, ki obisce 
# vsa mesta v seznamu natanko enkrat in se zakljuci v izhodiscnem mestu?

data("eurodist", package = "datasets")
D <- as.matrix(eurodist)
D

# Mesta ostevilcimo (dolocimo jim identifikacijske stevilke). Posamezen obhod je predstavljen kot permutacija 
# teh identifikacijskih stevilk. Njihov vrstni red doloca, v katerem vrstnem redu bomo obiskovali mesta.

# Izracun dolzine obhoda.
tourLength <- function(tour) 
{
	N <- length(tour)

	dist <- 0
	for (i in 2:N) 
		dist <- dist + D[tour[i-1],tour[i]]
	
	dist <- dist + D[tour[N],tour[1]]
	dist
}


# Kriterijska funkcija je obratno sorazmerna dolzini obhoda.
tspFitness <- function(tour) 
{
	1/tourLength(tour)
}

GA5 <- ga(type = "permutation", fitness = tspFitness, min = 1, max = ncol(D), popSize = 50, maxiter = 5000, run = 500, pmutation = 0.2)

summary(GA5)

# Iz permutacije zgradimo resitev.
tour <- GA5@solution[1, ]
tour <- c(tour, tour[1])

tourLength(tour)
colnames(D)[tour]


