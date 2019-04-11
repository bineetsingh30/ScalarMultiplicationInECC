$(function(){
    let divbuttons = $('#divbuttons')
    let signinform = $('#signinform')
    let signupform = $('#signupform')
    

    $('#signin').click(function(){
        divbuttons.hide()
        signinform.show()
    })

    $('#signup').click(function(){
        divbuttons.hide()
        signupform.show()
    })
})