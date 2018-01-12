##############################################################################################################################
#
# NALOGE
#
##############################################################################################################################
#
# - Z uporabo genetskega algoritma dolocite minimum funkcije f(x) = abs(x) + cos(x). Omejite iskanje na interval [-20, 20]. 
#   Premislite, kako boste definirali fitness funkcijo, saj jo ga() vedno maksimizira!  
#
#############################################################################################################################
#
# - Z uporabo genetskega algoritma doloèite minimum funkcije f(x1, x2) = 20 + x1^2 + x2^2 - 10*(cos(2*pi*x1) + cos(2*pi*x2)).
#   Pri iskanju omejite zalogo vrednosti x1 in x2 na interval [-5.12, 5.12].
#	
#############################################################################################################################
#
# - Opazovali smo nek proces in izmerili naslednje podatke:
#
#   Substrate <- c(1.73, 2.06, 2.20, 4.28, 4.44, 5.53, 6.32, 6.68, 7.28, 7.90, 8.80, 9.14, 9.18, 9.40, 9.88)
#   Velocity <- c(12.48, 13.97, 14.59, 21.25, 21.66, 21.97, 25.36, 22.93, 24.81, 25.63, 24.68, 29.04, 28.08, 27.32, 27.77)
#
#   Matematicni model, ki opisuje ta proces, je podan s formulo:
#   Velocity = (M * Substrate) / (K + Substrate), kjer sta M in K parametra modela. Z uporabo genetskega algoritma dolocite
#   vrednosti parametrov M in K modela, ki najbolje opise opazovane podatke. Pri iskanju omejite zalogo vrednosti 
#   parametra M na interval [40.0, 50.0] in parametra K na interval [3.0, 5.0].
#
#############################################################################################################################
#
# - Uporabite genetski algoritem za izbiro podmnozice atributov, ki minimizira srednjo kvadratno napako linearnega modela:
#
#   train.data <- read.table("AlgaeLearn.txt", header = T)
#   test.data <- read.table("AlgaeTest.txt", header = T)
#   lm.model <- lm(a1 ~., train.data)
#
#############################################################################################################################