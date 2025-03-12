document.addEventListener("DOMContentLoaded", function () {
    // Retrieve base URL and postId from hidden divs
    const contextPath = document.getElementById("contextPath").textContent.trim();
    const postId = document.getElementById("postId").textContent.trim();

    // Comment submission
    const commentForm = document.getElementById("commentForm");
    commentForm.addEventListener("submit", function (event) {
        event.preventDefault();
        const commentContentEl = document.getElementById("commentContent");
        const content = commentContentEl.value.trim();
        if (content === "") {
            alert("Comment cannot be empty.");
            return;
        }
        fetch(contextPath + "posts/addComment", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ postId: postId, content: content })
        })
            .then(function (response) {
                return response.json();
            })
            .then(function (data) {
                // Clear the comment content
                commentContentEl.value = "";
                // Append the new comment
                const newComment = document.createElement("li");
                newComment.className = "list-group-item";

                const commentContentDiv = document.createElement("div");
                commentContentDiv.className = "comment-content";
                commentContentDiv.style.cursor = "pointer";
                commentContentDiv.textContent = data.content;

                const commentEditTextarea = document.createElement("textarea");
                commentEditTextarea.className = "comment-edit form-control";
                commentEditTextarea.style.display = "none";
                commentEditTextarea.style.resize = "vertical";

                const deleteButton = document.createElement("button");
                deleteButton.className = "btn btn-danger btn-sm delete-comment";
                deleteButton.setAttribute("data-comment-id", data.id);
                deleteButton.setAttribute("data-post-id", data.postId);
                deleteButton.textContent = "Delete";

                newComment.appendChild(commentContentDiv);
                newComment.appendChild(commentEditTextarea);
                newComment.appendChild(deleteButton);

                document.getElementById("commentList").appendChild(newComment);
            })
            .catch(function (error) {
                console.error("Error:", error);
            });
    });

    // Update comment functionality
    document.getElementById("commentList").addEventListener("click", function (event) {
        if (event.target.classList.contains("comment-content")) {
            const commentContent = event.target;
            const commentEdit = commentContent.nextElementSibling;
            commentContent.style.display = "none";
            commentEdit.style.display = "block";
            commentEdit.value = commentContent.textContent.trim();
            commentEdit.focus();
        }
    });

    document.getElementById("commentList").addEventListener("keydown", function (event) {
        if (event.target.classList.contains("comment-edit")) {
            if (event.ctrlKey && event.key === "Enter") {
                const commentEdit = event.target;
                const commentContent = commentEdit.previousElementSibling;
                const commentItem = commentEdit.parentElement;
                const commentId = commentItem.querySelector(".delete-comment").getAttribute("data-comment-id");
                const newContent = commentEdit.value.trim();
                if (newContent === "") {
                    alert("Comment cannot be empty.");
                    return;
                }
                fetch(contextPath + "posts/updateComment", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ id: commentId, content: newContent })
                })
                    .then(function (response) {
                        if (!response.ok) {
                            throw new Error("Failed to update comment");
                        }
                        return response.json();
                    })
                    .then(function (data) {
                        commentContent.textContent = data.content;
                        commentEdit.style.display = "none";
                        commentContent.style.display = "block";
                    })
                    .catch(function (error) {
                        console.error("Error:", error);
                        alert("An error occurred while updating the comment.");
                        commentEdit.style.display = "none";
                        commentContent.style.display = "block";
                    });
            } else if (event.key === "Escape") {
                const commentEdit = event.target;
                const commentContent = commentEdit.previousElementSibling;
                commentEdit.style.display = "none";
                commentContent.style.display = "block";
            }
        }
    });

    // Delete comment functionality
    document.getElementById("commentList").addEventListener("click", function (event) {
        if (event.target.classList.contains("delete-comment")) {
            const commentId = event.target.getAttribute("data-comment-id");
            const postId = event.target.getAttribute("data-post-id");
            if (!confirm("Are you sure you want to delete this comment?")) {
                return;
            }
            fetch(contextPath + "posts/deleteComment?commentId=" + commentId + "&postId=" + postId, {
                method: "GET"
            })
                .then(function (response) {
                    if (response.ok) {
                        event.target.closest("li").remove();
                    }
                })
                .catch(function (error) {
                    console.error("Error:", error);
                    alert("An error occurred while deleting the comment.");
                });
        }
    });
});