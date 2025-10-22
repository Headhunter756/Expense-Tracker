var message_box;
var message = "";

window.addEventListener("DOMContentLoaded",()=>{

    getDetails(sessionStorage.getItem("username"));

    getImage(sessionStorage.getItem("username"));

    message_box = document.getElementById("message");
    message_box.style.color="red";
    if (message!=null) {
        message_box.textContent = message;
    }
    if (localStorage.getItem("message")!=null) {
        message_box.textContent = localStorage.getItem("message");
    }

});

async function getDetails(username) {
    const response = await fetch("https://techhunters-expense-tracker.up.railway.app/profile/details",{
        method: 'POST',
        headers:{
            'Authorization': sessionStorage.getItem("token"),
        },
        body:username
    });
    if (response.ok) {
        const data = await response.json();
        document.getElementById("id").textContent = data.id;
        document.getElementById("name").textContent = data.name;
        document.getElementById("age").textContent = data.age;
        document.getElementById("role").textContent = data.role;
        document.getElementById("email").textContent = data.email;
    }
    else{
        message += await response.text();
    }
}

async function getImage(username) {
    const response = await fetch('https://techhunters-expense-tracker.up.railway.app/profile/image', {
    method:'POST',
    headers: {
      'Authorization': sessionStorage.getItem("token")
    },
    body: username
    });
    if (response.ok) {
        const image = await response.blob();
        const url = URL.createObjectURL(image);
        document.getElementById('profileImage').src = url;
    } else {
        message += "Couldn't Find the image.";
    }
}

function changePassword() {
    window.location.href = "html/password.html";
}