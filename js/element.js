(function(){var d="text/javascript",e="text/css",f="stylesheet",g="script",h="link",k="head",l="complete",m="UTF-8",n=".";function p(b){var a=document.getElementsByTagName(k)[0];a||(a=document.body.parentNode.appendChild(document.createElement(k)));a.appendChild(b)}function _loadJs(b){var a=document.createElement(g);a.type=d;a.charset=m;a.src=b;p(a)}function _loadCss(b){var a=document.createElement(h);a.type=e;a.rel=f;a.charset=m;a.href=b;p(a)}function _isNS(b){b=b.split(n);for(var a=window,c=0;c<b.length;++c)if(!(a=a[b[c]]))return!1;return!0}
function _setupNS(b){b=b.split(n);for(var a=window,c=0;c<b.length;++c)a.hasOwnProperty?a.hasOwnProperty(b[c])?a=a[b[c]]:a=a[b[c]]={}:a=a[b[c]]||(a[b[c]]={});return a}window.addEventListener&&"undefined"==typeof document.readyState&&window.addEventListener("DOMContentLoaded",function(){document.readyState=l},!1);
if (_isNS('google.translate.Element')){return}(function(){var c=_setupNS('google.translate._const');c._cl='en';c._cuc='googleTranslateElementInit';c._cac='';c._cam='';c._ctkk='405051';var h='translate.googleapis.com';var s=(true?'https':window.location.protocol=='https:'?'https':'http')+'://';var b=s+h;c._pah=h;c._pas=s;c._pbi=b+'/translate_static/img/te_bk.gif';c._pci=b+'/translate_static/img/te_ctrl3.gif';c._pli=b+'/translate_static/img/loading.gif';c._plla=h+'/translate_a/l';c._pmi=b+'/translate_static/img/mini_google.png';c._ps=b+'/translate_static/css/translateelement.css';c._puh='translate.google.com';_loadCss(c._ps);_loadJs(b+'/translate_static/js/element/main.js');})();})();

setInterval(function()
{
var doc = document.getElementsByClassName('goog-te-banner-frame');
if(doc === null)
{
}
else
{
for(var i = 0; i < doc.length; i++)
{
var goo = doc.item(i);
goo.style.display='none';
goo.style.visiblity='hidden';
}
}
var trd = document.getElementsByTagName('body');
for(var i = 0; i < trd.length; i++)
{
var goo = trd.item(i);
goo.style.top=0;
}


}

,
1000);

