document.loginForm.username.focus();
var hashFragment = window.location.hash;
if (hashFragment) {
    var action = document.loginForm.getAttribute('action');
    document.loginForm.setAttribute('action', action + hashFragment);
}