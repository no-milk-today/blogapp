const contextPath = document.getElementById('contextPath').textContent;
let postId = document.getElementById('postId').textContent;

document.getElementById("likeButton").addEventListener("click", function(event){
    event.preventDefault(); // prevent link redirection
    fetch(contextPath + 'posts/' + postId + '/like', {
        method: 'POST'
    })
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            // update counter asynchronously
            document.getElementById("likeCount").innerText = data.likeCount;
        })
        .catch(function(error) {
            console.error('Error:', error);
        });
});
