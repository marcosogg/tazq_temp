package ie.setu.tazq.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ie.setu.tazq.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    fun signUp(name: String, email: String, password: String): Flow<Result<User>> = flow {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                val user = User(
                    uid = firebaseUser.uid,
                    name = name,
                    email = email
                )
                // Store user in Firestore
                firestore.collection("users")
                    .document(firebaseUser.uid)
                    .set(user)
                    .await()

                emit(Result.success(user))
            } ?: emit(Result.failure(Exception("User creation failed")))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    fun signIn(email: String, password: String): Flow<Result<User>> = flow {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                // Get user data from Firestore
                val userDoc = firestore.collection("users")
                    .document(firebaseUser.uid)
                    .get()
                    .await()

                val user = userDoc.toObject(User::class.java)
                    ?: User(uid = firebaseUser.uid, email = email)

                emit(Result.success(user))
            } ?: emit(Result.failure(Exception("Authentication failed")))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser
        return firebaseUser?.let {
            User(
                uid = it.uid,
                email = it.email ?: "",
                name = it.displayName ?: ""
            )
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
