pragma solidity >=0.4.22 <0.5.0;

pragma experimental ABIEncoderV2;

contract FoodInfoItem{
    uint[] _timestamp;     //保存食品流转过程中各个阶段的时间戳
    string[] _traceName;    //保存食品流转过程各个阶段的用户名
    address[] _traceAddress; //保存食品流转过程各个阶段的用户地址信息（和用户一一对应）
    uint8[] _traceQuality;  //保存食品流转过程中各个阶段的质量
    string _name;  //食物名称
    string _currentTraceName;  //当前用户名称
    uint8 _quality; //质量（0=优质 1=合格 2=不合格）
    uint8 _status; //状态（0:生产 1:分销 2:出售）
    address  _owner;

  constructor (string name, string traceName, uint8 quality, address producer) public {
        _timestamp.push(now);
        _traceName.push(traceName);
        _traceAddress.push(producer);
        _traceQuality.push(quality);
        _name = name;
        _currentTraceName = traceName;
        _quality = quality;
        _status = 0;
        _owner = msg.sender;
    }

    // 关于批发商distributor的食品溯源
    function addTraceInfoByDistributor( string traceName, address distributor, uint8 quality) public returns(bool) {
        require(_status == 0 , "status must be producing");
        require(_owner == msg.sender, "only trace contract can invoke");
        _timestamp.push(now);
        _traceName.push(traceName);
        _currentTraceName = traceName;
        _traceAddress.push(distributor);
        _quality = quality;
        _traceQuality.push(_quality);
        _status = 1;
        return true;
    }

    // 关于零售商的食品溯源
    function addTraceInfoByRetailer( string traceName, address retailer, uint8 quality) public returns(bool) {
        require(_status == 1 , "status must be distributing");
        require(_owner == msg.sender, "only trace contract can invoke");
        _timestamp.push(now);
        _traceName.push(traceName);
        _currentTraceName = traceName;
        _traceAddress.push(retailer);
        _quality = quality;
        _status = 2;
        return true;
    }

//    获取到溯源信息
    function getTraceInfo() public constant returns(uint[], string[], address[], uint8[]) {
        return(_timestamp, _traceName, _traceAddress, _traceQuality);
    }

//    获取到食品
    function getFood() public constant returns(uint, string, string, string, address, uint8) {
        return(_timestamp[0], _traceName[0], _name, _currentTraceName, _traceAddress[0], _quality);
    }

}
