package com.github.jianqibot.wxshop.controller;


import com.github.jianqibot.wxshop.entity.HttpException;
import com.github.jianqibot.wxshop.entity.PageResponse;
import com.github.jianqibot.wxshop.entity.Response;
import com.github.jianqibot.wxshop.generate.Goods;
import com.github.jianqibot.wxshop.service.GoodsService;
import com.github.jianqibot.wxshop.service.UserContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
public class GoodsController {
    private final GoodsService goodsService;

    @Inject
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    /**
     * @api {get} /goods fetch all goods
     * @apiName GetGoods
     * @apiGroup goods
     *
     * @apiHeader {String} Accept application/json
     *
     * @apiParam {Number} pageNum page number, from 1
     * @apiParam {Number} pageSize page size, how many item in one page
     * @apiParam {Number} [shopId] shop ID，if not null，only display items in this shop
     *
     * @apiSuccess {Number} pageNum page number, from 1
     * @apiSuccess {Number} pageSize page size, how many item in one page
     * @apiSuccess {Number} totalPage page in total
     * @apiSuccess {Goods} data goods list
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "pageNum": 1,
     *       "pageSize": 10,
     *       "totalPage": 5,
     *       "data": [
     *          {
     *              "id": 123,
     *              "name": "soap",
     *              "description": "coconut soap",
     *              "details": "coconut soap made with premium material",
     *              "imgUrl": "https://img.url",
     *              "price": 500,
     *              "stock": 10,
     *              "shopId": 12345,
     *              "createdAt": "2020-03-22T13:22:03Z",
     *              "updatedAt": "2020-03-22T13:22:03Z"
     *          },
     *          {
     *              ...
     *          }
     *       ]
     *     }
     *
     * @apiError 401 Unauthorized If user hasn't login
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 401 Unauthorized
     *     {
     *       "message": "Unauthorized"
     *     }
     */
    /**
     *
     * @param pageNum page number
     * @param pageSize page size
     * @param shopId shop ID
     * @return goods list
     */
    @GetMapping("/goods")
    @ResponseBody
    public PageResponse<Goods> getGoods(@RequestParam("pageNum") Integer pageNum,
                                        @RequestParam("pageSize") Integer pageSize,
                                        @RequestParam(value = "shopId", required = false) Long shopId) {

        return  goodsService.getGoods(pageNum, pageSize, shopId);
    }


    /**
     * @api {post} /goods Create Goods
     * @apiName CreateGoods
     * @apiGroup goods
     *
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     *
     * @apiParamExample {json} Request-Example:
     *          {
     *              "name": "soap",
     *              "description": "coconut soap",
     *              "details": "coconut soap made with premium material",
     *              "imgUrl": "https://img.url",
     *              "price": 500,
     *              "stock": 10,
     *              "shopId": 12345
     *          }
     *
     *
     * @apiSuccess {Goods} data Goods Created
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 201 Created
     *     {
     *       "data": {
     *              "id": 123,
     *              "name": "soap",
     *              "description": "coconut soap",
     *              "details": "coconut soap made with premium material",
     *              "imgUrl": "https://img.url",
     *              "price": 500,
     *              "stock": 10,
     *              "shopId": 12345,
     *              "createdAt": "2020-03-22T13:22:03Z",
     *              "updatedAt": "2020-03-22T13:22:03Z"
     *       }
     *     }
     *
     * @apiError 400 Bad Request If request contains error
     * @apiError 401 Unauthorized If user hasn't login yet
     * @apiError 403 Forbidden If user try to access shop which does not belong to him
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 401 Unauthorized
     *     {
     *       "message": "Unauthorized"
     *     }
     */

    /**
     * @param goods goods to be created
     * @param response the HTTP response
     * @return the newly created goods
     */
    @PostMapping("/goods")
    public Response<Goods> createGoods(@RequestBody Goods goods,
                                       HttpServletResponse response) {
        dataSanitization(goods);
        response.setStatus(HttpStatus.CREATED.value());
        try {
            return Response.of(goodsService.createGoods(goods, UserContext.getCurrentUser().getId()));
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.error(e.getMessage());
        }
    }

    /**
     * @api {patch} /goods/:id update goods
     * @apiName UpdateGoods
     * @apiGroup goods
     *
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     *
     * @apiParam {Number} id goods ID
     * @apiParamExample {json} Request-Example:
     *          {
     *              "name": "soap",
     *              "description": "coconut soap",
     *              "details": "coconut soap made with premium material",
     *              "imgUrl": "https://img.url",
     *              "price": 500,
     *              "stock": 10
     *          }
     *
     *
     * @apiSuccess {Goods} data updated goods
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "data": {
     *              "id": 123,
     *              "name": "soap",
     *              "description": "coconut soap",
     *              "details": "coconut soap made with premium material",
     *              "imgUrl": "https://img.url",
     *              "price": 500,
     *              "stock": 10,
     *              "createdAt": "2020-03-22T13:22:03Z",
     *              "updatedAt": "2020-03-22T13:22:03Z"
     *       }
     *     }
     *
     * @apiError 400 Bad Request If request contains error
     * @apiError 401 Unauthorized If user hasn't login yet
     * @apiError 403 Forbidden If user try to access shop which does not belong to him
     * @apiError 404 Not Found If goods was not found
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 401 Unauthorized
     *     {
     *       "message": "Unauthorized"
     *     }
     */
    /**
     *
     * @param goodsId goods ID
     * @param goods goods to be updated
     * @param response http response
     * @return response of Goods type
     */
    @RequestMapping(value = "/goods/{id}", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseBody
    public Response<Goods> updateGoods(@PathVariable("id") Long goodsId,
                                       @RequestBody Goods goods,
                                       HttpServletResponse response) {
        try {
            response.setStatus(HttpStatus.OK.value());
            return Response.of(goodsService.updateGoods(goodsId, goods, UserContext.getCurrentUser().getId()));
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.error(e.getMessage());
        }
    }


    /**
     * @api {delete} /goods/:id delete goods
     * @apiName DeleteGoods
     * @apiGroup goods
     *
     * @apiHeader {String} Accept application/json
     *
     * @apiParam {Number} id goods ID
     *
     * @apiSuccess {Goods} data deleted goods
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 204 No Content
     *     {
     *       "data": {
     *              "id": 12345,
     *              "name": "soap",
     *              "description": "coconut soap",
     *              "details": "coconut soap made with premium material",
     *              "imgUrl": "https://img.url",
     *              "price": 500,
     *              "stock": 10,
     *              "createdAt": "2020-03-22T13:22:03Z",
     *              "updatedAt": "2020-03-22T13:22:03Z"
     *       }
     *     }
     *
     * @apiError 400 Bad Request If request contains error
     * @apiError 401 Unauthorized If user hasn't login yet
     * @apiError 403 Forbidden If user try to access shop which does not belong to him
     * @apiError 404 Not Found If goods was not found
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 401 Unauthorized
     *     {
     *       "message": "Unauthorized"
     *     }
     */

    /**
     * @param goodsId the goods id to be deleted
     * @param response the HTTP response
     * @return the deleted goods
     */
    @DeleteMapping("/goods/{id}")
    @ResponseBody
    public Response<Goods> deleteGoods(@PathVariable("id") Long goodsId,
                                       HttpServletResponse response) {

        response.setStatus(HttpStatus.NO_CONTENT.value());
        try {
            Goods goods = new Goods();
            goods.setName("car");
            return Response.of(goods);
            //return Response.of(goodsService.deleteGoodsById(goodsId, UserContext.getCurrentUser().getId()));
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.error(e.getMessage());
        }
    }


    private void dataSanitization(Goods goods) {
        goods.setId(null);
        goods.setCreatedAt(LocalDateTime.now());
        goods.setUpdatedAt(LocalDateTime.now());
    }
}
