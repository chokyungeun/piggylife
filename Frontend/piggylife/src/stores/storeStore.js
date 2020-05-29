import { observable, action, computed } from "mobx";
import agent from "../agent";

export default class StoreStore {
  @observable myposts = [];
  @observable store_name="";
  @observable storeItems = [];
  @observable detailPost = {};

  @computed get mypostslength() {
    return this.myposts.length;
  }

  @action
  get_mypost() {
    console.log("내가 작성한 먹킷리스트 불러오기");
    // return agent.Data.get_mypost()
    //   .then((res) => {
    //     this.setMyPosts(res.data.results);
    //   })

    //   .catch((err) => alert("검색 결과가 없습니다."));
  }
  @action
  setMyPosts(myposts) {
    this.myposts = myposts;
  }

  @action search(store_name){
    this.store_name=store_name;
    return agent.Data.search(this.store_name)
      .then((res) => {
        // console.log(res.data);
        this.storeItems=res.data;        
        if(res.data.length === 0){
          alert("검색된 데이터가 없습니다.")
          window.location.reload();
        }
      })
      .catch((err) => alert("실패"))
  }

  @action detail(sid){
    return agent.Data.detail(sid)
      .then((res) => {
        this.detailPost = res.data;
        // console.log(res.data);
      })
      .catch((err) => alert("실패"))
  }

  @action upload(data){
    console.log(data)
    console.log(data.v_memo)
    console.log(data.isLike)
    console.log(data.sid)
    console.log(data.visited)
    // return agent.Data.upload(data)
    //   .then((res) => {

    //   })
    //   .catch((err) => alert("업로드 실패!"))
  }
}
