###########################################################################
#
# Metoda z iteracijo vrednosti
#
###########################################################################

#
# T[s,a] - prehod iz stanja s z akcijo a
# R[s,a] - nagrada, ki jo agent dobi, ko zapusti stanje s z akcijo a
#

valueIteration <- function(T, R, gamma = 0.9, iterations = 100)
{	
	#iniciliziraj zacetne vrednosti stanj na 0
	V <- rep(0, nrow(R))
	
	# mozne akcije
	A <- 1:ncol(R)
	
	# mozna stanja
	S <- 1:nrow(R)

	#inicilizacija Q (stanja X akcije)
	Q <- matrix(0, nrow(R), ncol(R))

	for(i in 1:iterations)
	{
		# za vsako stanje s
		for(s in S)
		{
			# za vsako akcijo a
			for(a in A)
			{
				Q[s,a] <- R[s,a] + gamma*1*V[T[s,a]]
				V[s] <- max(Q[s,], na.rm=TRUE)
			}
		
		}
	}
	
	V
}


###########################################################################
#
# Q-ucenje
#
###########################################################################

#
# T[s,a] - prehod iz stanja s z akcijo a
# R[s,a] - nagrada, ki jo agent dobi, ce v stanju s izvede akcijo a
# F - vektor koncnih stanj
#

qlearning <- function(T, R, F, gamma = 0.9)
{
       # stevilo stanj
       nstates <- nrow(T)
       nactions <- ncol(T)

       # incializiramo Q matriko na 0
       Q <- matrix(0, nrow = nstates, ncol = nactions)

       alpha <- 1

       while (alpha > 0.1)
       {
               # nakljucno izberemo zacetno stanje
               cur.state <- sample(1:nstates, 1)

               # dokler ne pridemo v koncno stanje
               while (!(cur.state %in% F))
               {
                       # katere akcije so na voljo iz trenutnega stanja
                       possible.actions <- which(!is.na(T[cur.state,]))

                       # nakljucno izberi akcijo
                       action <- possible.actions[sample(length(possible.actions), 1)]

                       # izbrana akcija doloca naslednje stanje
                       next.state <- T[cur.state, action]

                       # popravi Q za trenutno stanje in izbrano akcijo
                       Q[cur.state, action] <- Q[cur.state, action] + alpha * (R[cur.state, action] + gamma * max(Q[next.state,], na.rm = T) - Q[cur.state, action])

                       cur.state <- next.state
               }

               alpha <- alpha * 0.999
       }

       Q / max(Q)
}



################################################################################################
#
# Resitev naloge 1 (v dokumentu "Spodbujevanjo ucenje - naloge.pdf" na ucilnici)
# z uporabo q-ucenja
#
################################################################################################

# matrika prehodov
#
# vrstice predstavljajo sobe 
# stolpci predstavljajo vrata
# vrednost T[2,4] = 5 pomeni, da iz sobe 2 s prehodom skozi vrata 4 pridemo v sobo 5
#

T <- matrix(NA, nrow = 11, ncol = 12, byrow = TRUE)
T[1, 1] = 11
T[1, 2] = 2
T[2, 2] = 1
T[2, 4] = 5
T[3, 3] = 4
T[4, 3] = 3
T[4, 5] = 6
T[4, 12] = 11
T[5, 4] = 2
T[5, 6] = 6
T[5, 7] = 7
T[6, 5] = 4
T[6, 6] = 5
T[6, 8] = 9
T[7, 7] = 5
T[7, 10] = 8
T[8, 10] = 7
T[9, 8] = 6
T[9, 9] = 10
T[10, 9] = 9
T[10, 11] = 11
T[11, 1] = 1
T[11, 11] = 10
T[11, 12] = 4

T

# matrika nagrad
#
#
R <- matrix(-1, nrow = 11, ncol = 12, byrow = TRUE)
R[1, 1] = 100
R[4, 12] = 100
R[10, 11] = 100

R


q.mat <- qlearning(T, R, 11)

# izpisi optimalno strategijo
apply(q.mat, 1, function(x){which(x == max(x))})


################################################################################################
#
# Resitev iste naloge s pomocjo metode z iteracijo vrednosti
#
################################################################################################


# matrika nagrad
# POZOR: nagrada se dodeli, ko agent zapusti stanje s z akcijo a

R <- matrix(-1, nrow = 11, ncol = 12, byrow = TRUE)
R[11, 1] = 100
R[11, 11] = 100
R[11, 12] = 100



v.mat <- valueIteration(T, R, 0.9, 100)
v.mat

