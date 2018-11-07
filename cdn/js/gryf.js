// Create cookie
function createCookie(name, value, days) {
    var expires;
    if (days) {
        var date = new Date();
        date.setTime(date.getTime()+(days*24*60*60*1000));
        expires = "; expires="+date.toGMTString();
    }
    else {
        expires = "";
    }
    document.cookie = name+"="+value+expires+"; path=/";
}

// Read cookie
function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1,c.length);
        }
        if (c.indexOf(nameEQ) === 0) {
            return c.substring(nameEQ.length,c.length);
        }
    }
    return null;
}

// Erase cookie
function eraseCookie(name) {
    createCookie(name,"",-1);
}

var htmlTag = document.getElementsByTagName("html")[0];

var externalTags = document.querySelectorAll('a[rel="external"]'), i;
for (i = 0; i < externalTags.length; ++i) externalTags[i].setAttribute('target','_blank');

var accessbilityFontTags = document.querySelectorAll('#accessbility .font a[rel="font"]');
var cookieFontSize = readCookie('fontSize');
if (cookieFontSize != null) {
  for (i = 0; i < accessbilityFontTags.length; ++i) accessbilityFontTags[i].classList.remove('current');
  htmlTag.classList.add(cookieFontSize);
  document.querySelector('#accessbility .font .'+cookieFontSize).classList.add('current');
}

for (i = 0; i < accessbilityFontTags.length; ++i) {
  accessbilityFontTags[i].addEventListener('click', function(el){
    for (j = 0; j < accessbilityFontTags.length; ++j) {
      accessbilityFontTags[j].classList.remove('current');
      htmlTag.classList.remove(accessbilityFontTags[j].classList[0]);
    }
    createCookie("fontSize", el.target.classList[0], 30);
    el.target.classList.add('current');
    htmlTag.classList.add(el.target.classList[0]);
    return false;
  });
}

if (readCookie('contrastHigh')) htmlTag.classList.add('contrast-high');
document.querySelector('#accessbility .font a[rel="contrast"]').addEventListener('click', function(el){
  readCookie('contrastHigh')?eraseCookie('contrastHigh'):createCookie('contrastHigh', true, 30);
  htmlTag.classList.toggle('contrast-high');
});

