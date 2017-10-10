
* create-controls.sh (dépend de *.awk et des fichiers block.csv & jdc.csv) : permet de construire les tables ComplianceCheck* (public) pour les tests

* extract_all.sh (dépend de extract-and-rename-schema.sh et d'une base "chouette" opérationnelle) : permet d'extraire un ensemble d'offres (REFERENTIAL) d'une base. Le principe est le suivant : 
	1)- importer des données "saines" depuis BOIV (IHM). Attention, il faut convenir d'un PREFIX pour le nom du JDD (REFERENTIAL)
	2)- modifier ces données depuis l'IHM de sorte de provoquer un cas d'erreur de validation
	3)- utiliser extract_all.sh  script pour extraire ces données modifiées (un fichier sql sera produit dans le repertoire "./generated/"
	4)- modifier expected-result.csv pour ajouter ce jdd dans la liste et préciser les résultats de tests attendus
	
	extract_all.sh recupère les offres dont le nom correspond à un pattern (option -f) 
	
	
* create_ComplianceFileSetTests.py (dépend de expected_result.csv) : génére la classe de test ComplianceFileSetTests.java sur la base des tests définis dans expected_result.csv
	
  