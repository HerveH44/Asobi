import React from "react";
import {string, array, func} from "prop-types";
import {connect} from "react-redux";
import {changeSort} from "../../../actions/game";

const SortCards = ({sort, sortTypes, onChangeSort}) => (
    <div className="settings-sort-cards">
        Sort cards by:
        <div className='connected-container'>
            {sortTypes.map((val, key) =>
                <label key={key} className='radio-label connected-component'>
                    <input checked={val === sort}
                           className='radio-input'
                           name='sort-order'
                           onChange={onChangeSort}
                           type='radio'
                           value={val}/>
                    {val}
                </label>
            )}
        </div>
    </div>
);

SortCards.propTypes = {
    sort: string.isRequired,
    sortTypes: array.isRequired,
    onChangeSort: func.isRequired
};


const mapStateToProps = ({gameSettings}) => ({
    ...gameSettings
});

const mapDispatchToProps = {
    onChangeSort: changeSort
};

export default connect(mapStateToProps, mapDispatchToProps)(SortCards);
