import React from "react";
import {func, string} from "prop-types";
import {connect} from "react-redux";
import {onClickLog} from "../../../../actions/game";

const Log = ({gameType, onClickLog}) => (
    /DRAFT/.test(gameType)
        ? <div>
            <button className='connected-component'
                    onClick={onClickLog}>
                Download Draft Log
            </button>
        </div>
        : <div/>
);

Log.propTypes = {
    gameType: string.isRequired,
    onClickLog: func.isRequired
};

const mapStateToProps = ({gameState: {gameType}}) => ({
    gameType,
});

const mapDispatchToProps = {
    onClickLog
};

export default connect(mapStateToProps, mapDispatchToProps)(Log);
