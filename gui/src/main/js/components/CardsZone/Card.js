import React from "react"
import {func, object, string} from "prop-types"

const Card = ({card, title = "", onClick= ()=>{}, classNames = "", onMouseEnter, onMouseLeave}) => (
    <div className={classNames}
          onClick={onClick}
          onMouseEnter={onMouseEnter}
          onMouseLeave={onMouseLeave}>
        <img src={`http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=${card.multiverseid}&type=card`}
             alt={card.name}/>
    </div>
);


Card.propTypes = {
    card: object.isRequired,
    onClick: func,
    title: string,
    classNames: string,
    onMouseEnter: func,
    onMouseLeave: func
};

export default Card;

