var _max = 999999 ;
function control(id,f) {
        if (id) {
                var _i = f.indexOf("(");
                if (_i<0)
                        return ;
                var _f = f.substring(0,_i)+"(id"+(f.indexOf(")")==_i+1?"":",")+f.substring(_i+1);
                return eval(_f);
        }
}
function verifierNombreObligatoire(v,e,d){
        return verifierNombre(v,e,d,1);
}
function verifierNombre(v,e) {
        return verifierNombre(v,e,0,0);
}
function verifierNombre(v,e,d) {
        return verifierNombre(v,e,d,0);
}
function verifierNombre(v,e,d,o) {
        if (!d)
                d=0;
        if (o && !verifierObligatoire(v)) {
                return false ;
        }
        var _sep = "." ;
        var _n = v.value ;
        var vV = new String(_n).length;
        if(vV && isNaN(_n) == true)
        {
                alert("Le contenu du champ "+v.name.substring(6)+" n'est pas un nombre");
                return false;
        }
        var _i = new String(_n).indexOf(_sep);
        if (_i<0) {
                _n = _n + _sep;
                _i = _n.length-1 ;
        }
        var _e = new String(_n).substring(0,_i) ;
        var _d = new String(_n).substring(_i+1) ;
        if(_e.length>e || _d.length>d)
        {
                alert("Le contenu du champ "+v.name.substring(6)+" doit avoir le format d un nombre de type "+e+(d?_sep+d:""));
                return false;
        }
        return true;
}
function verifierLongueur(v,min,max) {
        if (!max)
                max=_max ;
        var str = new String(v.value);
        // cas special pour le champs texte formate
        if (str == "<P>&nbsp;</P>") {
	        str = "";
        }
        var vV = str.length;
        if (vV>=min && vV<=max)
                return true ;
        if (max==_max)
                alert("Le contenu du champ "+v.name.substring(6)+" doit avoir une longueur superieure a "+min+" caracteres");
        else
                alert("Le contenu du champ "+v.name.substring(6)+" doit avoir une longueur comprise entre "+min+" et 20000 caracteres");
        return false ;
}
function verifierObligatoire(v) {
		var str = new String(v.value);
		// cas spécial pour les champs de type list
		var EMTY_STRING_LIST = new String("<lc></lc>");
		if (str.substr(0,EMTY_STRING_LIST.length)==EMTY_STRING_LIST) {
	        str = "";
        }
        var vV = str.length;
        if (vV>0)
                return true ;
        alert("Le champ "+v.name.substring(6)+" est obligatoire");

        return false ;
}

function verifierDate(v) {
        d = new String(v.value) ;
        if (!d.length)
                return true ;

  // Cette fonction permet de vérifier la validité d'une date au format jj/mm/aa ou jj/mm/aaaa
  // Par Romuald

  e = new RegExp("^[0-9]{1,2}\/[0-9]{1,2}\/([0-9]{4})$");
  if (!e.test(d)) { // On teste l'expression régulière pour valider la forme de la date
        alert("Le champ "+v.name.substring(6)+" doit etre au format jj/mm/aaaa");
        return false; // Si pas bon, retourne faux
  }

  // On sépare la date en 3 variables pour vérification, parseInt() converti du texte en entier
  j = parseInt(d.split("/")[0], 10); // jour
  m = parseInt(d.split("/")[1], 10); // mois
  a = parseInt(d.split("/")[2], 10); // année

  // Si l'année n'est composée que de 2 chiffres on complète automatiquement
  if (a < 1000) {
    if (a < 89)  a+=2000; // Si a < 89 alors on ajoute 2000 sinon on ajoute 1900
    else a+=1900;
  }

  // Définition du dernier jour de février
  // Année bissextile si annnée divisible par 4 et que ce n'est pas un siècle, ou bien si divisible par 400
  if (a%4 == 0 && a%100 !=0 || a%400 == 0) fev = 29;
  else fev = 28;

  // Nombre de jours pour chaque mois
  nbJours = new Array(31,fev,31,30,31,30,31,31,30,31,30,31);

  // Enfin, retourne vrai si le jour est bien entre 1 et le bon nombre de jours, idem pour les mois, sinon retourn faux
  if ( m >= 1 && m <=12 && j >= 1 && j <= nbJours[m-1] )
        return true
  else {
        alert("Le champ "+v.name.substring(6)+" n est pas une date valide");
        return false; // Si pas bon, retourne faux
  }
  return true ;
}
