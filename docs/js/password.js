var validation = false;
var confimation = false;

function validate(){
    const rawpass = document.getElementById("rawpass").value;
    const hasUpper = /[A-Z]/.test(rawpass);
    const hasLower = /[a-z]/.test(rawpass);
    const hasSpecial = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(rawpass);
    const hasNum = /[0-9]/.test(rawpass);
    const messages = new Map([
        ['hasLower',"Doesn't have a Lower Case"],
        ['hasUpper',"Doesn't have an Upper Case"],
        ['hasSpecial',"Doesn't have a Special Characters"],
        ['hasNum',"Doesn't have a Number"]
    ])
    const validateResult = document.getElementById("validateResult");

    if (!hasUpper ||!hasLower ||!hasSpecial ||!hasNum) {
        validation = false;
        validateResult.style.color = "red";
        validateResult.textContent = "The Password ";
        if(!hasLower){
            validateResult.textContent += messages.get('hasLower');
        }
        else if(!hasUpper){
            validateResult.textContent += messages.get('hasUpper');
        }
        else if(!hasSpecial){
            validateResult.textContent += messages.get('hasSpecial');
        }
        else {
            validateResult.textContent += messages.get('hasNum');
        }
    }
    else{
        validation = true;
        validateResult.style.color = "green";
        validateResult.textContent = "The Password Follows the requirement";
    }
}

function confirm(){
    const rawpass = document.getElementById("rawpass").value;
    const confirmpass = document.getElementById("confirmpass").value;
    if(rawpass!=confirmpass){
        confimation = false;
        document.getElementById("confirmation").style.color = "red";
        document.getElementById("confirmation").textContent = "The password didn't match";
    }
    else{
        confimation = true;
        document.getElementById("confirmation").style.color = "green";
        document.getElementById("confirmation").textContent = "The password match";
    }
}

async function change() {
    const password = document.getElementById("confirmpass").value;
    const username = sessionStorage.getItem("username");
    const response = await fetch("https://techhunters-expense-tracker.up.railway.app/profile/change",{
        method:'POST',
        headers:{
            'Content-Type':'application/json',
            'Authorization':sessionStorage.getItem("token")
        },
        body:JSON.stringify({password,username})
    });
    if (response.ok) {
        const message = await response.text();
        localStorage.setItem("message",message);
        window.location.href = "html/profile.html";
    }
    else{
        const message = await response.text();
        alert("Some error occured: "+message);
    }
}
