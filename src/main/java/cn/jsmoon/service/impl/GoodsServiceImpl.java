package cn.jsmoon.service.impl;


import cn.jsmoon.entity.Goods;
import cn.jsmoon.repository.GoodsRepository;
import cn.jsmoon.service.GoodsService;
import cn.jsmoon.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * 商品Service实现类
 * @author Administrator
 *
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

	@Resource
	private GoodsRepository goodsRepository;

	@Override
	public List<Goods> findByTypeId(int typeId) {
		return goodsRepository.findByTypeId(typeId);
	}

	@Override
	public List<Goods> list(Goods goods, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable=PageRequest.of(page-1, pageSize, direction, properties);
        return goodsRepository.findAll((Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                search(goods, root, query, cb),pageable).getContent();
	}

	@Override
	public Long getCount(Goods goods) {
        return goodsRepository.count((Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                search(goods, root, query, cb));
	}

	/**
	 * 商品查询条件工具类
	 * @param goods
	 * @param root
	 * @param query
	 * @param cb
	 * @return
	 */
	private Predicate search(Goods goods, Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate=cb.conjunction();
        if(goods!=null){
            if(StringUtil.isNotEmpty(goods.getName())){
                predicate.getExpressions().add(cb.like(root.get("name"), "%"+goods.getName()+"%"));
            }
            if(goods.getType()!=null && goods.getType().getId()!=null && goods.getType().getId()!=1){
                predicate.getExpressions().add(cb.equal(root.get("type").get("id"), goods.getType().getId()));
            }
            if(StringUtil.isNotEmpty(goods.getCodeOrName())){
                predicate.getExpressions().add(cb.or(cb.like(root.get("code"), "%"+goods.getCodeOrName()+"%"), cb.like(root.get("name"), "%"+goods.getCodeOrName()+"%")));
            }
        }
        return predicate;
	}

	@Override
	public String getMaxGoodsCode() {
		return goodsRepository.getMaxGoodsCode();
	}

	@Override
	public void delete(Integer id) {
		goodsRepository.deleteById(id);
	}

	@Override
	public Goods findById(Integer id) {
		return goodsRepository.findById(id).get();
	}

	@Override
	public void save(Goods goods) {
		goodsRepository.save(goods);
	}

	@Override
	public List<Goods> listNoInventoryQuantityByCodeOrName(String codeOrName, Integer page, Integer pageSize,
                                                           Direction direction, String... properties) {
		Pageable pageable=PageRequest.of(page-1,pageSize, direction, properties);
		Page<Goods> pageGoods=goodsRepository.findAll(new Specification<Goods>() {
			
			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(StringUtil.isNotEmpty(codeOrName)){
					predicate.getExpressions().add(cb.or(cb.like(root.get("code"), "%"+codeOrName+"%"), cb.like(root.get("name"), "%"+codeOrName+"%")));
				}
				predicate.getExpressions().add(cb.equal(root.get("inventoryQuantity"), 0)); // 库存是0
				return predicate;
			}
		},pageable);
		return pageGoods.getContent();
	}

	@Override
	public Long getCountNoInventoryQuantityByCodeOrName(String codeOrName) {
		Long count=goodsRepository.count(new Specification<Goods>() {

			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(StringUtil.isNotEmpty(codeOrName)){
					predicate.getExpressions().add(cb.or(cb.like(root.get("code"), "%"+codeOrName+"%"), cb.like(root.get("name"), "%"+codeOrName+"%")));
				}
				predicate.getExpressions().add(cb.equal(root.get("inventoryQuantity"), 0)); // 库存是0
				return predicate;
			}
			
		});
		return count;
	}

	@Override
	public List<Goods> listHasInventoryQuantityByCodeOrName(String codeOrName, Integer page, Integer pageSize,
                                                            Direction direction, String... properties) {
		Pageable pageable=PageRequest.of(page-1,pageSize, direction, properties);
		Page<Goods> pageGoods=goodsRepository.findAll(new Specification<Goods>() {
			
			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(StringUtil.isNotEmpty(codeOrName)){
					predicate.getExpressions().add(cb.or(cb.like(root.get("code"), "%"+codeOrName+"%"), cb.like(root.get("name"), "%"+codeOrName+"%")));
				}
				predicate.getExpressions().add(cb.greaterThan(root.get("inventoryQuantity"), 0)); // 库存大于0
				return predicate;
			}
		},pageable);
		return pageGoods.getContent();
	}

	@Override
	public Long getCountHasInventoryQuantityByCodeOrName(String codeOrName) {
		Long count=goodsRepository.count(new Specification<Goods>() {

			@Override
			public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(StringUtil.isNotEmpty(codeOrName)){
					predicate.getExpressions().add(cb.or(cb.like(root.get("code"), "%"+codeOrName+"%"), cb.like(root.get("name"), "%"+codeOrName+"%")));
				}
				predicate.getExpressions().add(cb.greaterThan(root.get("inventoryQuantity"), 0)); // 库存大于0
				return predicate;
			}
			
		});
		return count;
	}

	@Override
	public List<Goods> listAlarm() {
		return goodsRepository.listAlarm();
	}

	


}
