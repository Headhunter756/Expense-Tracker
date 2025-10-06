window.addEventListener("DOMContentLoaded",()=>{
    getLastId();
});

async function getLastId() {
    const response = await fetch("http://localhost:8080/expense/id",{
        method: "GET",
        headers:{
            'Content-Type':"application/json",
            'Authorization':sessionStorage.getItem("token")
        },
    });
    const lastid = await response.json();
    document.getElementById("lastid").value = lastid.lastid;
}

async function add() {
    const formobj = document.getElementById("add");
    const formdata = new FormData(formobj);
    const data = Object.fromEntries(formdata);
    const response = await fetch("http://localhost:8080/expense/",{
        method: "POST",
        headers:{
            'Content-Type':"application/json",
            'Authorization':sessionStorage.getItem("token")
        },
        body: JSON.stringify(data)
    });
    const message = await response.text();
    if (response.status==201) {
        document.getElementById("message").style.color = "green";
    } else {
        document.getElementById("message").style.color = "red";
    }
    document.getElementById("message").textContent = message;
}