from scipy.sparse.linalg import svds

import pandas as pd
import numpy as np
import data
import recommend

# stores = data.getStore()
# posts = data.getPost()

# user_store_rating = posts.pivot(index='u_id', columns='s_id', values='is_like').fillna(0)
# # pivot table 값을 numpy matrix로
# matrix = user_store_rating.as_matrix()
# # 사용자 평균 평점
# user_ratings_mean = np.mean(matrix, axis=1)
# # 사용자-가게에 대해 평균 평점 뺀 것
# matrix_user_mean = matrix - user_ratings_mean.reshape(-1, 1)

# U, sigma, Vt = svds(matrix_user_mean, k=5)
# sigma = np.diag(sigma)
# svd_user_predicted_ratings = np.dot(np.dot(U, sigma), Vt) + user_ratings_mean.reshape(-1, 1)
# svd_preds = pd.DataFrame(svd_user_predicted_ratings, columns=user_store_rating.columns, index=user_store_rating.index)
# print(svd_preds)
# print(type(svd_preds))


# data.save_predict(svd_preds)

# df = recommend.convert_recommend(svd_preds, posts, stores)
# print(df)
# data.save_recommendation(df)

print(recommend.convert_matching(1, 3))
